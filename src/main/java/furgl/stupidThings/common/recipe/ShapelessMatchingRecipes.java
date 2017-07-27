package furgl.stupidThings.common.recipe;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class ShapelessMatchingRecipes extends ShapelessRecipes
{
	private ItemStack recipeOutput;

	public ShapelessMatchingRecipes(String group, ItemStack output, NonNullList<Ingredient> recipeItems) {
		super(group, output, recipeItems);
		this.recipeOutput = output;
	}

	@Override
	@Nullable
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack result = this.recipeOutput.copy();
		for (int i = 0; i < inv.getHeight(); ++i)
			for (int j = 0; j < inv.getWidth(); ++j) {
				ItemStack stack = inv.getStackInRowAndColumn(j, i);
				if (stack != null && stack.getItem() instanceof ItemArmor) {
					result.setTagCompound(stack.getTagCompound());
					result.setItemDamage(stack.getItemDamage());
				}
			}
		return result;
	}

}
