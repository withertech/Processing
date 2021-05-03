package com.withertech.processing.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class Greetings
{
	private static final Greetings INSTANCE = new Greetings();

	private final List<Function<PlayerEntity, Optional<ITextComponent>>> messages = new ArrayList<>();

	private Greetings()
	{
		MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
	}

	public static void addMessage(Function<PlayerEntity, ITextComponent> message)
	{
		INSTANCE.messages.add(player -> Optional.ofNullable(message.apply(player)));
	}

	private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (player == null) return;
		messages.forEach(msg -> msg.apply(player).ifPresent(text -> player.sendMessage(text, Util.DUMMY_UUID)));
	}
}