package com.withertech.processing.network;


import com.withertech.processing.Processing;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Objects;

public final class Network
{
	private static final String VERSION = "silmech-net2";

	public static SimpleChannel channel;

	static
	{
		channel = NetworkRegistry.ChannelBuilder.named(Processing.getId("network"))
				.clientAcceptedVersions(s -> Objects.equals(s, VERSION))
				.serverAcceptedVersions(s -> Objects.equals(s, VERSION))
				.networkProtocolVersion(() -> VERSION)
				.simpleChannel();

		channel.messageBuilder(SetRedstoneModePacket.class, 1)
				.decoder(SetRedstoneModePacket::fromBytes)
				.encoder(SetRedstoneModePacket::toBytes)
				.consumer(SetRedstoneModePacket::handle)
				.add();
		channel.messageBuilder(LoginPacket.Reply.class, 2)
				.loginIndex(LoginPacket::getLoginIndex, LoginPacket::setLoginIndex)
				.decoder(buffer -> new LoginPacket.Reply())
				.encoder((msg, buffer) ->
				{
				})
				.consumer(FMLHandshakeHandler.indexFirst((hh, msg, ctx) -> msg.handle(ctx)))
				.add();
	}

	private Network()
	{
	}

	public static void init()
	{
	}
}
