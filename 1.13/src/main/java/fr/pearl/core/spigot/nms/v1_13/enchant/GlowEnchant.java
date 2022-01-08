package fr.pearl.core.spigot.nms.v1_13.enchant;

import fr.pearl.core.spigot.CoreSpigot;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GlowEnchant extends Enchantment {

    private static final NamespacedKey key = new NamespacedKey(CoreSpigot.getInstance(), CoreSpigot.getInstance().getDescription().getName());

    public GlowEnchant() {
        super(key);
    }

    @Override
    @NotNull
    public String getName() {
        return "Glow";
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    @NotNull
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack itemStack) {
        return true;
    }
}
