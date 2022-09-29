@file:Suppress("unused")

package cn.idealframework2.json

import com.fasterxml.jackson.core.type.TypeReference

fun Any.toJsonString() = JsonUtils.toJsonString(this)

fun <T> String.parseJson(type: TypeReference<T>) = JsonUtils.parse(this, type)

fun <T> String.parseJson(clazz: Class<T>) = JsonUtils.parse(this, clazz)

fun <T> String.parseJson(parametrized: Class<out Any>, parameterClass: Class<out Any>): T {
  return JsonUtils.parse<T>(this, parametrized, parameterClass)
}

fun <E> String.parseJsonList(clazz: Class<E>): List<E> = JsonUtils.parseList(this, clazz)

fun <E> String.parseJsonSet(clazz: Class<E>): Set<E> = JsonUtils.parseSet(this, clazz)
