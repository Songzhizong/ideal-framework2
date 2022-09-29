package cn.idealframework2.spring

import cn.idealframework2.transmission.Paging
import cn.idealframework2.transmission.SortablePaging
import org.springframework.data.domain.Page

fun Paging.toPageable() = SpringPageConverter.pageable(this)

fun SortablePaging.toPageable() = SpringPageConverter.pageable(this)

fun <E> Page<E>.toPageResult() = SpringPageConverter.pageResult(this)

fun <E, R> Page<E>.toPageResult(converter: (E) -> R) =
  SpringPageConverter.pageResult(this, converter)

