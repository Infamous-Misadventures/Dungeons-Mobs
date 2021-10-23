package com.infamous.dungeons_mobs.network.datasync;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModDataSerializers {

    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, DungeonsMobs.MODID);

    public static final IDataSerializer<List<UUID>> UUID_LIST = getUUIDListSerializer();

    private static IDataSerializer<List<UUID>> getUUIDListSerializer() {
        IDataSerializer<List<UUID>> serializer = new IDataSerializer<List<UUID>>() {
            public void write(PacketBuffer packetBuffer, List<UUID> UUIDList) {
                packetBuffer.writeInt(UUIDList.size());
                UUIDList.forEach(packetBuffer::writeUUID);
            }

            public List<UUID> read(PacketBuffer packetBuffer) {
                int i = packetBuffer.readInt();
                ArrayList<UUID> uuidList = new ArrayList<>();
                for (int j = 0; j < i; j++) {
                    uuidList.add(packetBuffer.readUUID());
                }
                return uuidList;
            }

            public List<UUID> copy(List<UUID> UUIDList) {
                return UUIDList;
            }
        };
        DATA_SERIALIZERS.register("uuid_list", () -> new DataSerializerEntry(serializer));
        return serializer;
    }
}