package fr.pearl.core.spigot.nms.v1_8;

import fr.pearl.api.spigot.nms.PearlNms;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.core.spigot.nms.v1_8.enchant.GlowEnchant;
import fr.pearl.core.spigot.nms.v1_8.scoreboard.Scoreboard;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.AntiXray;

public class Nms implements PearlNms<EntityPlayer> {

    @Override
    public void registerChannel(Player player, String baseName, String name, ChannelHandler handler) {
        this.getEntityPlayer(player).playerConnection.networkManager.channel.pipeline().addBefore(baseName, name, handler);
    }

    @Override
    public void removeChannel(Player player, String name) {
        Channel channel = this.getEntityPlayer(player).playerConnection.networkManager.channel;
        ChannelHandler handler = channel.pipeline().get("pearl-handler");
        if (handler != null) channel.pipeline().remove(handler);
    }

    @Override
    public void sendPacket(Player player, Object object) {
        getEntityPlayer(player).playerConnection.sendPacket((Packet<?>) object);
    }

    @Override
    public void setListName(Player player, String listName) {
        getEntityPlayer(player).listName = CraftChatMessage.fromString(listName)[0];
    }

    @Override
    public NmsScoreboard<ScoreboardServer> createScoreboard() {
        return new Scoreboard();
    }

    @Override
    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    @Override
    public Enchantment getGlowEnchant() {
        return new GlowEnchant();
    }

    @Override
    public void setUnbreakable(ItemMeta meta, boolean unbreakable) {
        meta.spigot().setUnbreakable(unbreakable);
    }
}
