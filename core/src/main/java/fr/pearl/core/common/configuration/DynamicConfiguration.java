package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.dynamic.*;
import fr.pearl.api.common.util.Reflection;
import org.apache.commons.lang.StringUtils;
import org.simpleyaml.configuration.ConfigurationSection;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class DynamicConfiguration extends Configuration {

    private final Object instance;

    public DynamicConfiguration(Object instance, File file) {
        super(file);

        this.instance = instance;
        ConfigurationHeader header = instance.getClass().getDeclaredAnnotation(ConfigurationHeader.class);
        if (header != null) {
             this.yamlFile.options().copyHeader(true).header(StringUtils.join(header.value(), "\n"));
             this.yamlFile.options().copyDefaults(false);
        }
    }

    @Override
    public void load() {
        super.load();
        this.loadFields(null, this.instance);
    }

    @Override
    public void save() {
        this.saveFields(null, this.instance);
        super.save();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadFields(String parent, Object instance) {
        for (Class<?> superClass : this.getSuperClasses(instance)) {
            for (Field field : superClass.getDeclaredFields()) {
                Reflection.setAccessible(field);
                ConfigurationKeys keys = field.getAnnotation(ConfigurationKeys.class);
                if (keys != null) {
                    Object value = Reflection.get(field, instance);
                    if (value instanceof ConfigurationList) {
                        ConfigurationList list = (ConfigurationList) value;
                        String[] keyComments = keys.keyComments();
                        Consumer<String> setComments = keyComments.length != 0 ? s -> this.yamlFile.setComment(s, StringUtils.join(keyComments, "\n")) : s -> {};
                        String path = (parent == null ? "" : parent + ".") + keys.value();
                        String[] comments = keys.comments();
                        if (comments.length > 0) {
                            this.yamlFile.setComment(path, StringUtils.join(comments, "\n"), keys.commentType());
                        }
                        ConfigurationSection section = this.yamlFile.getConfigurationSection(path);
                        if (section == null) {
                            String[] defaultKeys = keys.defaultKeys();
                            for (int i = 0; i < list.size(); i++) {
                                if (i >= defaultKeys.length) break;
                                ConfigurationPart part = (ConfigurationPart) list.get(i);
                                String keyName = defaultKeys[i];
                                String keyPath = path + "." + keyName;
                                setComments.accept(keyPath);
                                this.loadFields(keyPath, part);
                                part.setName(keyName);
                                part.loaded();
                            }

                            continue;
                        }
                        list.clear();
                        for (String key : section.getKeys(false)) {
                            ConfigurationPart part = list.create(key);
                            String keyPath = path + "." + key;
                            setComments.accept(keyPath);
                            this.loadFields(keyPath, part);
                            part.setName(key);
                            part.loaded();
                            list.add(part);
                        }

                    }
                    continue;
                }
                ConfigurationPath configurationPath = field.getAnnotation(ConfigurationPath.class);
                if (configurationPath == null) continue;
                String path = (parent == null ? "" : parent + ".") + configurationPath.value();
                Object value = Reflection.get(field, instance);
                String[] comments = configurationPath.comments();
                if (comments.length > 0) {
                    this.yamlFile.setComment(path, StringUtils.join(comments, "\n"), configurationPath.commentType());
                }
                if (value instanceof ConfigurationPart) {
                    ConfigurationPart part = (ConfigurationPart) value;
                    this.loadFields(path, part);
                    part.loaded();
                    continue;
                }
                Object configValue = this.yamlFile.get(path, value);
                Class<?> exceptedClass = value.getClass();
                if (exceptedClass == Float.class) exceptedClass = Double.class;
                if (configValue != null && exceptedClass.isAssignableFrom(configValue.getClass())) {
                    if (value.getClass() == Float.class) {
                        double doubleValue = (double) configValue;
                        value = (float) doubleValue;
                    } else {
                        value = configValue;
                    }
                }

                if (value instanceof String) {
                    String string = (String) value;
                    value = string.replaceAll("&", "§");
                } else if (value instanceof List) {
                    List list = (List) value;
                    Object object = list.size() == 0 ? null : list.get(0);
                    if (object instanceof String) {
                        List<String> coloredList = new ArrayList<>(list.size());
                        for (Object o : list) {
                            String string = (String) o;
                            coloredList.add(string.replaceAll("&", "§"));
                        }
                        value = coloredList;
                    }
                }

                Reflection.set(field, instance, value);
            }
        }

    }

    @SuppressWarnings("rawtypes")
    private void saveFields(String parent, Object instance) {
        for (Class<?> superClass : this.getSuperClasses(instance)) {
            for (Field field : superClass.getDeclaredFields()) {
                Reflection.setAccessible(field);
                ConfigurationKeys keys = field.getAnnotation(ConfigurationKeys.class);
                if (keys != null) {
                    String path = (parent == null ? "" : parent + ".") + keys.value();
                    Object value = Reflection.get(field, instance);
                    if (value instanceof ConfigurationList) {
                        ConfigurationList list = (ConfigurationList) value;
                        for (Object object : list) {
                            ConfigurationPart key = (ConfigurationPart) object;
                            this.saveFields(path + "." + key.getName(), key);
                        }
                    }
                    continue;
                }
                ConfigurationPath configurationPath = field.getAnnotation(ConfigurationPath.class);
                if (configurationPath == null) continue;
                String path = (parent == null ? "" : parent + ".") + configurationPath.value();
                Object value = Reflection.get(field, instance);
                if (value instanceof ConfigurationPart) {
                    ConfigurationPart part = (ConfigurationPart) value;
                    this.saveFields(path, part);
                    continue;
                }
                if (value instanceof String) {
                    String string = (String) value;
                    value = string.replaceAll("§", "&");
                } else if (value instanceof List) {
                    List list = (List) value;
                    Object object = list.size() == 0 ? null : list.get(0);
                    if (object instanceof String) {
                        List<String> coloredList = new ArrayList<>(list.size());
                        for (Object o : list) {
                            String string = (String) o;
                            coloredList.add(string.replaceAll("§", "&"));
                        }
                        value = coloredList;
                    }
                }
                this.yamlFile.set(path, value);
            }
        }
    }

    private List<Class<?>> getSuperClasses(Object instance) {
        LinkedList<Class<?>> classes = new LinkedList<>();
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            classes.addFirst(clazz);
            clazz = clazz.getSuperclass();
            if (clazz.isAssignableFrom(ConfigurationPart.class) || clazz == Object.class) clazz = null;
        }

        return classes;
    }
}
