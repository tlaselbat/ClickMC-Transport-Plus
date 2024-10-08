package com.tabletmc.transport_plus.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "transport_plus")
public class ModConfig implements ConfigData  {
    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean noWander = true;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean rubberBand = true;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    public boolean noBuck = true;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    public boolean swimHorse = true;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    public boolean swimCamel = false;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    public boolean swimDead = false;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public FadeConfig pitchFade = new FadeConfig();

//    @ConfigEntry.Category("Server")
//    @ConfigEntry.Gui.Tooltip
//    public boolean portalPatch = true;

//    @ConfigEntry.Category("Server")
//    @ConfigEntry.Gui.Tooltip
//    public boolean breakSpeed = true;

//    @ConfigEntry.Category("Server")
//    @ConfigEntry.Gui.Tooltip
//    public boolean stepHeight = true;

    public static class FadeConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 90)
        public int startAngle = 30;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 90)
        public int endAngle = 50;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 50, max = 100)
        public int maxTransparency = 90;
    }

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 45)
    public int horseHeadAngleOffset = 0;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    public boolean jeb_Horses = true;

    public static void registerConfig() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}