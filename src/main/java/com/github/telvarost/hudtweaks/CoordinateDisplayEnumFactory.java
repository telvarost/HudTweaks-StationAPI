package com.github.telvarost.hudtweaks;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi3.api.*;
import net.glasslauncher.mods.gcapi3.impl.SeptFunction;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import net.glasslauncher.mods.gcapi3.impl.object.entry.EnumConfigEntryHandler;

import java.lang.reflect.*;
import java.util.function.*;

public class CoordinateDisplayEnumFactory implements ConfigFactoryProvider {

    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(CoordinateDisplayEnum.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) ->
        {
            int enumOrdinal;
            if(enumOrOrdinal instanceof Integer ordinal) {
                enumOrdinal = ordinal;
            }
            else {
                enumOrdinal = ((CoordinateDisplayEnum) enumOrOrdinal).ordinal();
            }
            return new EnumConfigEntryHandler<CoordinateDisplayEnum>(id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrdinal, ((CoordinateDisplayEnum) defaultEnum).ordinal(), CoordinateDisplayEnum.class);
        }));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(CoordinateDisplayEnum.class, enumEntry -> enumEntry);
    }
}
