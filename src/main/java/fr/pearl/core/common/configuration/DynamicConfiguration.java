package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.ConfigurationException;
import fr.pearl.api.common.configuration.PearlSection;
import fr.pearl.api.common.configuration.dynamic.*;
import fr.pearl.api.common.util.Reflection;
import fr.pearl.core.common.configuration.comment.CommentNode;
import fr.pearl.core.common.configuration.comment.CommentTree;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicConfiguration extends Configuration {

    private final Object instance;
    private final Map<String, String[]> comments = new HashMap<>();
    private final String[] headers;

    private static final Pattern LIST_ENTRY_PATTERN = Pattern.compile("^(\\s*)-.+$");
    private static final Pattern SECTION_PATTERN = Pattern.compile("^(\\s*)[a-zA-Z0-9_-]+:$");
    private static final Pattern VALUE_PATTERN = Pattern.compile("^(\\s*)[a-zA-Z0-9_-]+: .+$");

    private static MatchResult matchResult(String line) {
        Matcher matcher;
        if (LIST_ENTRY_PATTERN.matcher(line).matches()) {
            return null;
        } else if ((matcher = SECTION_PATTERN.matcher(line)).matches()) {
            return matcher.toMatchResult();
        } else if ((matcher = VALUE_PATTERN.matcher(line)).matches()) {
            return matcher.toMatchResult();
        }

        return null;
    }

    public DynamicConfiguration(Object instance, File file) {
        super(file);

        this.instance = instance;
        ConfigurationHeader header = instance.getClass().getDeclaredAnnotation(ConfigurationHeader.class);
        this.headers = header == null ? null : header.value();
    }

    @Override
    public void load() {
        super.load();
        this.loadFromInstance(null, this.instance, this);
    }

    @Override
    public void save() {
        if (this.entries.isEmpty()) return;

        String dump = this.yaml.dump(this.entries);
        StringBuilder values = new StringBuilder();
        if (this.headers != null) {
            for (String header : this.headers) {
                if (header.equals("")) {
                    values.append("\n");
                } else {
                    values.append("# ").append(header).append("\n");
                }
            }
        }
        try (BufferedReader reader = new BufferedReader(new StringReader(dump))) {
            String line;
            CommentNode node = null;
            while ((line = reader.readLine()) != null) {
                MatchResult result = matchResult(line);
                String path = line.trim().split(":")[0];
                if (result != null) {
                    int indent = result.group(1).length();
                    node = CommentTree.getNode(node, path, indent);
                    String[] comments = this.comments.get(node.getFullPath());
                    if (comments != null) {
                        String separator = node.getIndentSeparator();
                        for (String comment : comments) {
                            values.append(separator);
                            if (!comment.equals("")) {
                                values.append("# ").append(comment);
                            }
                            values.append("\n");
                        }
                    }
                }
                values.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new ConfigurationException("Cannot save configuration file (" + this.file.getPath() + ")", e);
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8)) {
            writer.write(values.toString());
        } catch (IOException e) {
            throw new ConfigurationException("Cannot save configuration file (" + this.file.getPath() + ")", e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void loadFromInstance(String parentPath, Object instance, PearlSection parent) {
        for (Class<?> superClass : getSuperClasses(instance)) {
            for (Field field : superClass.getDeclaredFields()) {
                ConfigurationPath annotatedPath = field.getAnnotation(ConfigurationPath.class);
                ConfigurationKeys annotatedKeys = field.getAnnotation(ConfigurationKeys.class);
                if (annotatedPath == null) continue;
                Reflection.setAccessible(field);
                String path = annotatedPath.value();
                String fullPath;
                String commentPath;
                String annotatedCommentPath = annotatedPath.commentPath();
                if (parentPath == null) {
                    fullPath = path;
                    commentPath = annotatedCommentPath.equals("") ? path : annotatedCommentPath;
                } else {
                    fullPath = parentPath + "." + path;
                    commentPath = annotatedCommentPath.equals("") ? fullPath : parentPath + "." + path;
                }
                Object fieldValue = Reflection.get(field, instance);
                String[] comments = annotatedPath.comments();
                if (comments != null) {
                    this.comments.put(commentPath, comments);
                }
                if (fieldValue instanceof ConfigurationPart) {
                    PearlSection section = parent.getSection(path);
                    ConfigurationPart part = (ConfigurationPart) fieldValue;
                    this.loadFromInstance(fullPath, part, section);
                    part.loaded();
                    continue;
                } else if (fieldValue instanceof ConfigurationList) {
                    ConfigurationList list = (ConfigurationList) fieldValue;
                    PearlSection section = parent.getSection(path);
                    Map<String, Object> entries = section.getEntries();
                    String[] defaultComments = annotatedKeys == null ? null : annotatedKeys.keyComments();
                    if (entries.isEmpty()) {
                        String[] defaultKeys = annotatedKeys == null ? null : annotatedKeys.defaultKeys();
                        if (defaultKeys != null) {
                            for (int i = 0; i < list.size(); i++) {
                                ConfigurationSection keySection = new ConfigurationSection();
                                if (i >= defaultKeys.length) break;
                                ConfigurationPart part = (ConfigurationPart) list.get(i);
                                String keyName = defaultKeys[i];
                                String keyPath = fullPath + "." + keyName;
                                this.loadFromInstance(keyPath, part, keySection);
                                part.setName(keyName);
                                part.loaded();
                                if (defaultComments != null) {
                                    this.comments.put(keyPath, defaultComments);
                                }
                                section.set(keyName, keySection);
                            }
                        }
                    } else {
                        list.clear();
                        for (Map.Entry<String, Object> entry : entries.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            PearlSection keySection;
                            if (!(value instanceof PearlSection)) {
                                keySection = new ConfigurationSection();
                                entry.setValue(keySection);
                            } else {
                                keySection = (PearlSection) value;
                            }
                            String keyPath = fullPath + "." + key;
                            if (defaultComments != null) {
                                this.comments.put(keyPath, defaultComments);
                            }
                            ConfigurationPart part = list.create(key);
                            this.loadFromInstance(keyPath, part, keySection);
                            part.setName(key);
                            part.loaded();
                            list.add(part);
                        }
                    }
                    continue;
                }

                Object configValue = parent.get(path, fieldValue);
                if (configValue instanceof String) {
                    configValue = ((String) configValue).replaceAll("&", "§");
                } else if (configValue instanceof List) {
                    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    boolean isString = String.class.getName().equals(genericType.getActualTypeArguments()[0].getTypeName());
                    if (isString) {
                        List<String> currentList = (List<String>) configValue;
                        List<String> coloredList = new ArrayList<>(currentList.size());
                        for (String string : currentList) {
                            coloredList.add(string.replaceAll("&", "§"));
                        }
                        configValue = coloredList;
                    }
                }
                Reflection.set(field, instance, configValue);
            }
        }
    }

    private List<Class<?>> getSuperClasses(Object instance) {
        LinkedList<Class<?>> classes = new LinkedList<>();
        Class<?> superClass = instance.getClass();
        while (superClass != null) {
            classes.addFirst(superClass);
            superClass = superClass.getSuperclass();
        }

        return classes;
    }

}
