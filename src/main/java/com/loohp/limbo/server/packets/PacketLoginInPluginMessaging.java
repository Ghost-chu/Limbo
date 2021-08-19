package com.loohp.limbo.server.packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Optional;

import com.loohp.limbo.utils.DataTypeIO;

public class PacketLoginInPluginMessaging extends PacketIn {

	private int messageId;
	private boolean successful;
	private Optional<byte[]> data;

	public PacketLoginInPluginMessaging(int messageId, boolean successful, byte[] data) {
		this.messageId = messageId;
		this.successful = successful;
		this.data = successful ? Optional.of(data) : Optional.empty();
	}
	
	public PacketLoginInPluginMessaging(DataInputStream in, int packetLength, int packetId) throws IOException {
		messageId = DataTypeIO.readVarInt(in);
		successful = in.readBoolean();
		if (successful) {
			int dataLength = packetLength - DataTypeIO.getVarIntLength(packetId) - DataTypeIO.getVarIntLength(messageId) - 1;
			if (dataLength != 0) {
				byte[] data = new byte[dataLength];
				in.readFully(data);
				this.data = Optional.of(data);
			} else {
				this.data = Optional.of(new byte[0]);
			}
		} else {
			data = Optional.empty();
		}
	}
	
	public int getMessageId() {
		return messageId;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public Optional<byte[]> getData() {
		return data;
	}

}
