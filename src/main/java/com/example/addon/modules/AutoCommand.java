package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class AutoCommand extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("Der Command oder Chat-Text, der gesendet wird.")
        .defaultValue("/home")
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Abstand in Ticks (20 Ticks = 1 Sekunde).")
        .defaultValue(200)
        .min(1)
        .sliderRange(20, 1200)
        .build()
    );

    private final Setting<Boolean> once = sgGeneral.add(new BoolSetting.Builder()
        .name("once")
        .description("Sendet nur einmal und schaltet sich danach aus.")
        .defaultValue(false)
        .build()
    );

    private int timer;

    public AutoCommand() {
        super(AddonTemplate.CATEGORY, "auto-command", "Sendet automatisch einen Command in den Chat.");
    }

    @Override
    public void onActivate() {
        timer = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null ) return;

        if (++timer >= delay.get()) {
            ChatUtils.sendPlayerMsg(command.get());
            timer = 0;
            if (once.get()) toggle();
        }
    }
}
