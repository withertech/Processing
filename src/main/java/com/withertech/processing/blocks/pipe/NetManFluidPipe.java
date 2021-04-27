package com.withertech.processing.blocks.pipe;


import com.withertech.processing.Processing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = Processing.MODID)
public final class NetManFluidPipe
{
	private static final Collection<LazyOptional<NetFluidPipe>> NETWORK_LIST = Collections.synchronizedList(new ArrayList<>());
	public static boolean tickEnable = true;

	private NetManFluidPipe()
	{
		throw new IllegalAccessError("Utility class");
	}

	@SuppressWarnings("ConstantConditions")
	@Nullable
	public static NetFluidPipe get(IWorldReader world, BlockPos pos)
	{
		return getLazy(world, pos).orElse(null);
	}

	public static LazyOptional<NetFluidPipe> getLazy(IWorldReader world, BlockPos pos)
	{
		synchronized (NETWORK_LIST)
		{
			for (LazyOptional<NetFluidPipe> network : NETWORK_LIST)
			{
				if (network != null && network.isPresent())
				{
					NetFluidPipe net = network.orElseThrow(IllegalStateException::new);
					if (net.contains(world, pos))
					{
//						UnderPressure.LOGGER.debug("get network {}", network);
						return network;
					}
				}
			}
		}

		// Create new
		NetFluidPipe network = NetFluidPipe.buildNetwork(world, pos, FluidStack.EMPTY);
		LazyOptional<NetFluidPipe> lazy = LazyOptional.of(() -> network);
		NETWORK_LIST.add(lazy);
		Processing.LOGGER.debug("create network {}", network);
		return lazy;
	}

	public static void update(IWorldReader world, BlockPos pos, FluidStack stack)
	{
		invalidateNetwork(world, pos);
		NetFluidPipe network = NetFluidPipe.buildNetwork(world, pos, stack);
		LazyOptional<NetFluidPipe> lazy = LazyOptional.of(() -> network);
		NETWORK_LIST.add(lazy);
		Processing.LOGGER.debug("create network {}", network);
	}

	public static void update(IWorldReader world, BlockPos pos)
	{
		FluidStack contents;
		if (get(world, pos) != null)
		{
			contents = Objects.requireNonNull(get(world, pos)).getFluidInTank(1).copy();
		} else
		{
			contents = FluidStack.EMPTY;
		}
		update(world, pos, contents);
	}

	public static void invalidateNetwork(IWorldReader world, BlockPos pos)
	{
		Collection<LazyOptional<NetFluidPipe>> toRemove = new ArrayList<>();
		NETWORK_LIST.removeIf(Objects::isNull);
		for (LazyOptional<NetFluidPipe> n : NETWORK_LIST)
		{
			if (n != null && n.isPresent() && n.orElseThrow(IllegalStateException::new).contains(world, pos))
			{
				toRemove.add(n);
			}
		}
		toRemove.forEach(NetManFluidPipe::invalidateNetwork);
	}

	private static void invalidateNetwork(LazyOptional<NetFluidPipe> network)
	{
		Processing.LOGGER.debug("invalidateNetwork {}", network);
		NETWORK_LIST.removeIf(n -> n.isPresent() && n.equals(network));
		network.ifPresent(NetFluidPipe::invalidate);
		network.invalidate();
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event)
	{
		NETWORK_LIST.removeIf(Objects::isNull);
		for (Iterator<LazyOptional<NetFluidPipe>> netIt = NETWORK_LIST.stream().filter(n -> Objects.nonNull(n) && n.isPresent()).iterator(); netIt.hasNext(); )
		{
			LazyOptional<NetFluidPipe> net = netIt.next();
			net.ifPresent(NetFluidPipe::moveFluids);
		}
	}
}
