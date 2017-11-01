package galinhavoadora.wurmunlimited.client.cheats;

import java.util.Properties;

import javax.swing.DebugGraphics;

import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;

public class WUClientPlayerModifiers implements WurmClientMod, PreInitable, Configurable{

	private static float speedModifier = 1.0f;
	
	@Override
	public void configure(Properties properties) {
		this.speedModifier = Float.valueOf(properties.getProperty("speedModifier", Float.toString(this.speedModifier))).floatValue();
	}

	@Override
	public void preInit() {
		this.lockSpeedModifier();
		
	}
	
	private void lockSpeedModifier() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.shared.util.MovementChecker");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("setSpeedModifier", Descriptor.ofMethod(CtPrimitiveType.voidType, parameters));
            method.insertBefore("if ($1 < "+speedModifier+") $1 = "+speedModifier+";");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            parameters = new CtClass[]{};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method = ex.getMethod("getSpeedMod", Descriptor.ofMethod(CtPrimitiveType.floatType, parameters));
            method.insertBefore("if (this.speedMod < "+speedModifier+") return "+speedModifier+";");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            
            parameters = new CtClass[]{CtPrimitiveType.intType, CtPrimitiveType.intType, CtPrimitiveType.intType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method = ex.getMethod("getSpeedForTile", Descriptor.ofMethod(CtPrimitiveType.floatType, parameters));
            method.setBody("return 1.0f;");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}

}
