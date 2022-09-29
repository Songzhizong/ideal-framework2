package cn.idealframework2.utils

import cn.idealframework2.lang.TreeNode

fun <E : TreeNode> Collection<E>.toTreeList(): List<E> = TreeNode.toTreeList(this)
