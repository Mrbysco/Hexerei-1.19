package net.joefoxe.hexerei.block;

import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.List;

public class ModWoodType {
    public static WoodType WILLOW = WoodType.create("hexerei_willow");
    public static WoodType POLISHED_WILLOW = WoodType.create("hexerei_polished_willow");
    public static WoodType WITCH_HAZEL = WoodType.create("hexerei_witch_hazel");
    public static WoodType POLISHED_WITCH_HAZEL = WoodType.create("hexerei_polished_witch_hazel");
    public static WoodType MAHOGANY = WoodType.create("hexerei_mahogany");
    public static WoodType POLISHED_MAHOGANY = WoodType.create("hexerei_polished_mahogany");
    public static List<WoodType> woodTypes = List.of(WILLOW, MAHOGANY, WITCH_HAZEL, POLISHED_WILLOW, POLISHED_MAHOGANY, POLISHED_WITCH_HAZEL);
}