/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.joshariasSurvival.systems;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.location.LocationComponent;
import org.terasology.machines.ExtendedInventoryManager;
import org.terasology.math.geom.Vector3i;
import org.terasology.network.ClientComponent;
import org.terasology.registry.In;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.items.BlockItemComponent;
import org.terasology.world.block.items.BlockItemFactory;
import org.terasology.world.block.items.OnBlockItemPlaced;

import java.util.ArrayList;

@RegisterSystem
public class DemoSystem extends BaseComponentSystem {
    @In
    BlockManager blockManager;
    @In
    InventoryManager inventoryManager;
    @In
    EntityManager entityManager;
    @In
    WorldProvider worldProvider;
    @In
    BlockEntityRegistry blockEntityRegistry;

    @Command(shortDescription = "Gives items to demonstrate Josharias Survival", runOnServer = true)
    public String jsStarterPack(@CommandParam(value = "module", required = false) String pack, @Sender EntityRef client) {
        BlockItemFactory blockFactory = new BlockItemFactory(entityManager);
        EntityRef player = client.getComponent(ClientComponent.class).character;
        ArrayList<String> items = new ArrayList<>();
        if (pack == null || pack.equalsIgnoreCase("ManualLabor")) {
            inventoryManager.giveItem(player, EntityRef.NULL, createSupplyChest(
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:CrudeHammer", 1),
                    blockFactory.newInstance(blockManager.getBlockFamily("Stone"), 32),
                    blockFactory.newInstance(blockManager.getBlockFamily("IronOre"), 32),
                    blockFactory.newInstance(blockManager.getBlockFamily("CoalOre"), 32),
                    blockFactory.newInstance(blockManager.getBlockFamily("OakTrunk"), 32),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Mallet", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:MetalFile", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Saw", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Pliers", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Screwdriver", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Wrench", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:SledgeHammer", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Axe", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Pickaxe", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Shovel", 1),
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:MagnifyingGlass", 1)
            ));
            inventoryManager.giveItem(player, EntityRef.NULL, createSupplyChest(
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:Plank", 32)
            ));
            inventoryManager.giveItem(player, EntityRef.NULL, createSupplyChest(
                    ExtendedInventoryManager.createItem(entityManager, "ManualLabor:WoodenBucket", 1)
            ));
            items.add("AssemblyTable");
            items.add("ToolAssemblyTable");
            items.add("Firebox");
            items.add("Hearth");
            items.add("Sifter");
            items.add("Campfire");
        }


        if (pack == null || pack.equalsIgnoreCase("IRLCorp")) {
            inventoryManager.giveItem(player, EntityRef.NULL, createSupplyChest(
                    ExtendedInventoryManager.createItem(entityManager, "WindmillSail", 1),
                    blockFactory.newInstance(blockManager.getBlockFamily("WoodenAxle"), 32)
            ));
            inventoryManager.giveItem(player, EntityRef.NULL, createSupplyChest(
                    ExtendedInventoryManager.createItem(entityManager, "SubstanceMatters:MaterialItem#ManualLabor:Chunks|SubstanceMatters:Coal", 99),
                    blockFactory.newInstance(blockManager.getBlockFamily("Axle"), 32)
            ));
            inventoryManager.giveItem(player, EntityRef.NULL, createSupplyChest(
                    blockFactory.newInstance(blockManager.getBlockFamily("ConveyorBelt"), 32)
            ));
            inventoryManager.giveItem(player, EntityRef.NULL, createSupplyChest(
                    blockFactory.newInstance(blockManager.getBlockFamily("FluidPipe"), 32)
            ));
            items.add("Windmill");
            items.add("WoodenAxle");
            items.add("WoodenGearBox");
            items.add("Axle");
            items.add("GearBox");
            items.add("Engine");
            items.add("Crusher");
            items.add("Sawmill");
            items.add("Grinder");
            items.add("FrictionHeater");
            items.add("ConveyorBelt");
            items.add("ItemExtractor");
            items.add("FluidPump");
            items.add("FluidPipe");
            items.add("FluidTank");
        }
        
        for (String itemName : items) {
            inventoryManager.giveItem(player, EntityRef.NULL, blockFactory.newInstance(blockManager.getBlockFamily(itemName), 1));
        }

        return "You received the Josharias Survival " + pack + " starter pack";
    }

    private EntityRef createSupplyChest(EntityRef... items) {
        BlockItemFactory blockFactory = new BlockItemFactory(entityManager);

        EntityRef chest = blockFactory.newInstance(blockManager.getBlockFamily("CoreBlocks:Chest"), 1);
        chest.addComponent(new InventoryComponent(items.length));

        for (EntityRef item : items) {
            inventoryManager.giveItem(chest, chest, item);
        }

        return chest;
    }

    @Command(shortDescription = "Places items in a line", runOnServer = true)
    public String jsPlaceInventory(@Sender EntityRef client) {
        EntityRef character = client.getComponent(ClientComponent.class).character;
        LocationComponent locationComponent = character.getComponent(LocationComponent.class);
        Vector3i characterPos = new Vector3i(locationComponent.getWorldPosition(), 0.5f);

        int itemsPlaced = 0;
        int supplyChestsPlaced = 0;
        for (EntityRef item : ExtendedInventoryManager.iterateItems(inventoryManager, character)) {
            BlockItemComponent blockItemComponent = item.getComponent(BlockItemComponent.class);
            if (blockItemComponent != null) {
                Vector3i pos = new Vector3i(characterPos.x + itemsPlaced * 2 + 2, characterPos.y, characterPos.z);
                if (blockItemComponent.blockFamily.getURI().equals(new BlockUri("CoreBlocks:Chest"))) {
                    pos = new Vector3i(pos.x - 2, pos.y, pos.z + 2);
                    supplyChestsPlaced++;
                } else {
                    itemsPlaced++;
                }

                worldProvider.setBlock(pos, blockItemComponent.blockFamily.getArchetypeBlock());
                item.send(new OnBlockItemPlaced(pos, blockEntityRegistry.getBlockEntityAt(pos)));
                item.destroy();
            }
        }

        return itemsPlaced + " blocks placed";
    }
}
