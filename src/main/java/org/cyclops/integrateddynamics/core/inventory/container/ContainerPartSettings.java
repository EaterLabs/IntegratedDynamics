package org.cyclops.integrateddynamics.core.inventory.container;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;
import org.cyclops.cyclopscore.inventory.container.ExtendedInventoryContainer;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.core.network.packet.ActionGetUpdateIntervalPacket;
import org.cyclops.integrateddynamics.core.part.IPartContainer;
import org.cyclops.integrateddynamics.core.part.IPartState;
import org.cyclops.integrateddynamics.core.part.IPartType;
import org.cyclops.integrateddynamics.core.part.PartTarget;

/**
 * Container for part settings.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ContainerPartSettings extends ExtendedInventoryContainer {

    public static final int BUTTON_SETTINGS = 1;
    private static final int PAGE_SIZE = 3;

    private final PartTarget target;
    private final IPartContainer partContainer;
    private final IPartType partType;
    private final World world;
    private final BlockPos pos;

    /**
     * Make a new instance.
     * @param target The target.
     * @param player The player.
     * @param partContainer The part container.
     * @param partType The part type.
     */
    public ContainerPartSettings(EntityPlayer player, PartTarget target, IPartContainer partContainer, IPartType partType) {
        super(player.inventory, (IGuiContainerProvider) partType);
        this.target = target;
        this.partContainer = partContainer;
        this.partType = partType;
        this.world = player.getEntityWorld();
        this.pos = player.getPosition();

        addPlayerInventory(player.inventory, 8, 31);

        if(!MinecraftHelpers.isClientSide()) {
            IntegratedDynamics._instance.getPacketHandler().sendToPlayer(
                    new ActionGetUpdateIntervalPacket(getPartType().getUpdateInterval(getPartState())), player);
        }
    }

    @SuppressWarnings("unchecked")
    public IPartState getPartState() {
        return partContainer.getPartState(getTarget().getCenter().getSide());
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    protected int getSizeInventory() {
        return 0;
    }
}
