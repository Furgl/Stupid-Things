package furgl.stupidThings.packet;

import java.util.UUID;

import furgl.stupidThings.common.StupidThings;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWorldsSmallestViolinSound implements IMessage {
	
	private UUID player;

	public PacketWorldsSmallestViolinSound() {}

	public PacketWorldsSmallestViolinSound(UUID player) {
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.player = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, player.toString());
	}

	public static class Handler implements IMessageHandler<PacketWorldsSmallestViolinSound, IMessage> {
		@Override
		public IMessage onMessage(final PacketWorldsSmallestViolinSound packet, final MessageContext ctx) {
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByUUID(packet.player);
					if (player != null)
						StupidThings.proxy.playWorldsSmallestViolinSound(player);
				}
			});
			return null;
		}
	}
}