package com.infamous.dungeons_mobs.network.datasync;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModDataSerializers {

    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, DungeonsMobs.MODID);

    public static final RegistryObject<EntityDataSerializer<List<UUID>>> UUID_LIST = DATA_SERIALIZERS.register("uuid_list", () -> getUUIDListSerializer());

    private static EntityDataSerializer<List<UUID>> getUUIDListSerializer() {
        return new EntityDataSerializer<List<UUID>>() {
            public void write(FriendlyByteBuf packetBuffer, List<UUID> UUIDList) {
                packetBuffer.writeInt(UUIDList.size());
                UUIDList.forEach(packetBuffer::writeUUID);
            }

            public List<UUID> read(FriendlyByteBuf packetBuffer) {
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
    }
}