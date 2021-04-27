package com.withertech.processing.blocks.pipe;

import com.withertech.processing.api.ConnectionType;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public final class NetFluidPipe implements IFluidHandler
{
	private final IWorldReader world;
	private final Map<BlockPos, Set<Connection>> connections = new HashMap<>();
	private final FluidTank fluidTank;
	private boolean connectionsBuilt;

	private NetFluidPipe(IWorldReader world, Set<BlockPos> pipes, FluidStack contents)
	{
		this.world = world;
		for (BlockPos pos : pipes)
		{
			connections.put(pos, Collections.emptySet());
		}
		this.fluidTank = new FluidTank(8000);
		this.fluidTank.setFluid(contents);
	}

	static NetFluidPipe buildNetwork(IWorldReader world, BlockPos pos, FluidStack contents)
	{
		Set<BlockPos> pipes = buildPipeSet(world, pos);
		return new NetFluidPipe(world, pipes, contents);
	}

	private static Set<BlockPos> buildPipeSet(IWorldReader world, BlockPos pos)
	{
		return buildPipeSet(world, pos, new HashSet<>());
	}

	private static Set<BlockPos> buildPipeSet(IWorldReader world, BlockPos pos, Set<BlockPos> set)
	{
		// Get all positions that have a wire connected to the wire at pos
		set.add(pos);
		for (Direction side : Direction.values())
		{
			BlockPos pos1 = pos.offset(side);
			if (!set.contains(pos1) && world.getTileEntity(pos1) instanceof TileFluidPipe)
			{
				set.add(pos1);
				set.addAll(buildPipeSet(world, pos1, set));
			}
		}
		return set;
	}

	@Nullable
	private static IFluidHandler getFluidHandler(IBlockReader world, BlockPos pos, Direction side)
	{
		TileEntity tileEntity = world.getTileEntity(pos.offset(side));
		if (tileEntity != null)
		{
			//noinspection ConstantConditions
			return tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).orElse(null);
		}
		return null;
	}

	public boolean contains(IWorldReader world, BlockPos pos)
	{
		return this.world == world && connections.containsKey(pos);
	}

	public int getPipeCount()
	{
		return connections.size();
	}

	public Connection getConnection(BlockPos pos, Direction side)
	{
		if (connections.containsKey(pos))
		{
			for (Connection connection : connections.get(pos))
			{
				if (connection.side == side)
				{
					return connection;
				}
			}
		}
		return new Connection(this, side, ConnectionType.NONE);

	}

	void invalidate()
	{
		for (Set<Connection> set : connections.values())
		{
			for (Connection con : set)
			{
				con.getLazyOptional().invalidate();
			}
		}
	}

	private void buildConnections()
	{
		// Determine all connections. This will be done once the connections are actually needed.
		if (!connectionsBuilt)
		{
			Map<BlockPos, Set<Connection>> connectionsTmp = new HashMap<>();
			for (BlockPos blockPos : connections.keySet())
			{
				connectionsTmp.put(blockPos, getConnections(world, blockPos));
			}
			for (Map.Entry<BlockPos, Set<Connection>> entry : connectionsTmp.entrySet())
			{
				BlockPos p = entry.getKey();
				Set<Connection> c = entry.getValue();
				connections.put(p, c);
			}
			connectionsBuilt = true;
		}
	}

	private Set<Connection> getConnections(IBlockReader world, BlockPos pos)
	{
		// Get all connections for the wire at pos
		Set<Connection> connections = new HashSet<>();
		for (Direction direction : Direction.values())
		{
			TileEntity te = world.getTileEntity(pos.offset(direction));
			if (te != null && !(te instanceof TileFluidPipe) && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent() && world.getBlockState(pos).getBlock() != Blocks.AIR)
			{
				ConnectionType type = BlockFluidPipe.getConnection(world.getBlockState(pos), direction);
				connections.add(new Connection(this, direction, type));
			}
		}
		return connections;
	}

	void moveFluids()
	{

		buildConnections();

		for (Map.Entry<BlockPos, Set<Connection>> entry_i : connections.entrySet())
		{
			for (Map.Entry<BlockPos, Set<Connection>> entry_o : connections.entrySet())
			{
				BlockPos pos_o = entry_o.getKey();
				Set<Connection> connections_o = entry_o.getValue();
				BlockPos pos_i = entry_i.getKey();
				Set<Connection> connections_i = entry_i.getValue();
				for (Connection con_i : connections_i)
				{

					if (con_i.type.canExtract())
					{
						IFluidHandler fluidHandler_i = getFluidHandler(world, pos_i, con_i.side);
						if (fluidHandler_i != null && (getTankCapacity(1) - getFluidInTank(1).getAmount()) > 1)
						{
							FluidStack toSend = fluidHandler_i.drain(10, FluidAction.EXECUTE);
							if (!toSend.isEmpty())
							{
								if (fill(toSend, FluidAction.EXECUTE) > 0)
								{

								}
							}
						}
					}
				}
				for (Connection con_o : connections_o)
				{
					if (con_o.type.canReceive())
					{
						IFluidHandler fluidHandler_o = getFluidHandler(world, pos_o, con_o.side);
						if (fluidHandler_o != null && (fluidHandler_o.getTankCapacity(1) - fluidHandler_o.getFluidInTank(1).getAmount()) > 1)
						{
							FluidStack toSend = drain(10, FluidAction.EXECUTE);
							if (!toSend.isEmpty())
							{
								if (fluidHandler_o.fill(toSend, FluidAction.EXECUTE) > 0)
								{
								}
							}
						} else
						{
							break;
						}
					}
				}
			}
		}

	}

	@Override
	public String toString()
	{
		return String.format("PipeNetwork %s, %d pipes", Integer.toHexString(hashCode()), connections.size());
	}

	@Override
	public int getTanks()
	{
		return 1;
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank)
	{
		return fluidTank.getFluidInTank(tank);
	}

	@Override
	public int getTankCapacity(int tank)
	{
		return fluidTank.getTankCapacity(0);
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
	{
		return fluidTank.isFluidValid(tank, stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action)
	{
		return fluidTank.fill(resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action)
	{
		return fluidTank.drain(resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action)
	{
		return fluidTank.drain(maxDrain, action);
	}

	public static class Connection implements IFluidHandler
	{
		private final NetFluidPipe network;
		private final Direction side;
		private final ConnectionType type;
		private final LazyOptional<Connection> lazyOptional;

		Connection(NetFluidPipe network, Direction side, ConnectionType type)
		{
			this.network = network;
			this.side = side;
			this.type = type;
			this.lazyOptional = LazyOptional.of(() -> this);
		}

		public LazyOptional<Connection> getLazyOptional()
		{
			return lazyOptional;
		}

		@Override
		public int getTanks()
		{
			return 1;
		}

		@Nonnull
		@Override
		public FluidStack getFluidInTank(int tank)
		{
			return network.fluidTank.getFluid();
		}

		@Override
		public int getTankCapacity(int tank)
		{
			return network.fluidTank.getTankCapacity(tank);
		}

		@Override
		public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
		{
			return network.fluidTank.isFluidValid(tank, stack);
		}

		@Override
		public int fill(FluidStack resource, FluidAction action)
		{
			if (!type.canReceive())
			{
				return 0;
			}
			return network.fluidTank.fill(resource, action);
		}

		@Nonnull
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action)
		{
			if (!type.canExtract())
			{
				return FluidStack.EMPTY;
			}
			return network.fluidTank.drain(resource, action);
		}

		@Nonnull
		@Override
		public FluidStack drain(int maxDrain, FluidAction action)
		{
			if (!type.canExtract())
			{
				return FluidStack.EMPTY;
			}
			return network.fluidTank.drain(maxDrain, action);
		}
	}
}
