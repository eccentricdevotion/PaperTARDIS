/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.helpers;

import io.netty.channel.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiser;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import me.eccentric_nz.tardischunkgenerator.custombiome.BiomeHelper;
import net.minecraft.core.Registry;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

public class TARDISPacketListener {

    private static Field connectionField;

    public static void removePlayer(Player player) {
        Connection connection = getConnection(((CraftPlayer) player).getHandle().connection);
        Channel channel = connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    private static Connection getConnection(final ServerCommonPacketListenerImpl playerConnection) {
        try {
            if (connectionField == null) {
                connectionField = ServerCommonPacketListenerImpl.class.getDeclaredField("connection");
                connectionField.setAccessible(true);
            }
            return (Connection) connectionField.get(playerConnection);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                if (packet instanceof ClientboundAddEntityPacket namedEntitySpawn && !TARDIS.plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
                    UUID uuid = namedEntitySpawn.getUUID();
                    if (TARDISDisguiseTracker.DISGUISED_AS_MOB.containsKey(uuid)) {
                        Entity entity = Bukkit.getEntity(uuid);
                        if (entity.getType().equals(EntityType.PLAYER)) {
                            Player player = (Player) entity;
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> TARDISDisguiser.redisguise(player, entity.getWorld()), 5L);
                        }
                    }
                }
                if (TARDISHelper.colourSkies && packet instanceof ClientboundLevelChunkWithLightPacket chunkPacket) {
                    String world = player.getWorld().getName();
                    if (world.equals("gallifrey") || world.equals("skaro")) {
                        LevelChunk levelChunk = cloneChunk((LevelChunk) ((CraftChunk) player.getWorld().getChunkAt(chunkPacket.getX(), chunkPacket.getZ())).getHandle(ChunkStatus.BIOMES));
                        String key = (world.endsWith("gallifrey")) ? "gallifrey_badlands" : "skaro_desert";
                        Biome biome = TARDISHelper.biomeMap.get(key);
                        Registry<Biome> registry = BiomeHelper.getRegistry();
                        if (biome != null) {
                            for (LevelChunkSection section : levelChunk.getSections()) {
                                for (int x = 0; x < 4; ++x) {
                                    for (int z = 0; z < 4; ++z) {
                                        for (int y = 0; y < 4; ++y) {
                                            section.setBiome(x, y, z, registry.wrapAsHolder(biome));
                                        }
                                    }
                                }
                            }
                            packet = new ClientboundLevelChunkWithLightPacket(levelChunk, levelChunk.getLevel().getLightEngine(), null, null);
                        } else {
                            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "biome was null");
                        }
                    }
                }
                super.write(channelHandlerContext, packet, channelPromise);
            }
        };
        Connection connection = getConnection(((CraftPlayer) player).getHandle().connection);
        ChannelPipeline pipeline = connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName() + "_tcg", channelDuplexHandler);
    }

    private static LevelChunk cloneChunk(LevelChunk chunk) {
        return new LevelChunk(
                chunk.getLevel(),
                chunk.getPos(),
                chunk.getUpgradeData(),
                (LevelChunkTicks<Block>) chunk.getBlockTicks(),
                (LevelChunkTicks<Fluid>) chunk.getFluidTicks(),
                chunk.getInhabitedTime(),
                chunk.getSections(),
                null,
                chunk.getBlendingData()
        );
    }
}
