/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftRegionAccessor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.LimitedRegion;

public class CustomTree {

    public static void grow(TARDISTree tree, Location location) {
        TARDISTreeData data;
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        switch (tree) {
            case GALLIFREY_SAND -> data = TARDISFeatures.GALLIFREY_TREE_RED_SAND;
            case GALLIFREY_TERRACOTTA -> data = TARDISFeatures.GALLIFREY_TREE_TERRACOTTA;
            case SKARO -> data = TARDISFeatures.SKARO_TREE;
            default -> data = TARDISFeatures.RANDOM_TREE; // RANDOM
        }
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        BlockPos pos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        new TreePlacer().place(data, level, pos, chunkGenerator);
    }

    public static void grow(TARDISTree tree, int x, int y, int z, LimitedRegion limitedRegion) {
        TARDISTreeData data;
        WorldGenLevel level = ((CraftRegionAccessor) limitedRegion).getHandle();
        switch (tree) {
            case GALLIFREY_SAND -> data = TARDISFeatures.GALLIFREY_TREE_RED_SAND;
            case GALLIFREY_TERRACOTTA -> data = TARDISFeatures.GALLIFREY_TREE_TERRACOTTA;
            case SKARO -> data = TARDISFeatures.SKARO_TREE;
            default -> data = TARDISFeatures.RANDOM_TREE; // RANDOM
        }
        ChunkGenerator chunkGenerator = level.getMinecraftWorld().getChunkSource().getGenerator();
        BlockPos pos = new BlockPos(x, y, z);
        new TreePlacer().place(data, level, pos, chunkGenerator);
    }

    public static void grow(Location location, Material base, Material stem, Material hat, Material decor) {
        TARDISTreeData data = new TARDISTreeData(base, stem, hat, decor, true);
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        BlockPos pos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        new TreePlacer().place(data, level, pos, chunkGenerator);
    }
}
