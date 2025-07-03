package com.github.telvarost.hudtweaks.enums;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi3.impl.SeptFunction;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import net.glasslauncher.mods.gcapi3.impl.object.entry.EnumConfigEntryHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

public class ScreenPositionHorizontalEnumFactory implements ConfigFactoryProvider {

    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(ScreenPositionHorizontalEnum.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) ->
        {
            int enumOrdinal;
            if(enumOrOrdinal instanceof Integer ordinal) {
                enumOrdinal = ordinal;
            }
            else {
                enumOrdinal = ((ScreenPositionHorizontalEnum) enumOrOrdinal).ordinal();
            }
            return new EnumConfigEntryHandler<ScreenPositionHorizontalEnum>(id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrdinal, ((ScreenPositionHorizontalEnum) defaultEnum).ordinal(), ScreenPositionHorizontalEnum.class);
        }));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(ScreenPositionHorizontalEnum.class, enumEntry -> enumEntry);
    }
}
