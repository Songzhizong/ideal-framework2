package cn.idealframework2.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.*;

/**
 * 树节点接口
 *
 * @author 宋志宗 on 2021/1/19
 */
public interface TreeNode {

  /**
   * 将列表转换为树
   *
   * @param source 源列表
   * @param <E>    TreeNode的实现类
   * @return 树结构的列表
   * @author 宋志宗 on 2022/9/5
   */
  @Nonnull
  static <E extends TreeNode> List<E> toTreeList(@Nonnull Collection<E> source) {
    int size = source.size();
    if (size < 2) {
      return new ArrayList<>(source);
    }
    List<E> result = new ArrayList<>();
    Map<Object, E> sourceMap = new HashMap<>(Math.max((int) (size / 0.75F) + 1, 16));
    for (E e : source) {
      if (e != null) {
        Object nodeId = e.getNodeId();
        sourceMap.put(nodeId, e);
      }
    }
    for (E e : source) {
      if (e == null) {
        continue;
      }
      Object parentNodeId = e.getParentNodeId();
      E node = null;
      if (parentNodeId != null) {
        node = sourceMap.get(parentNodeId);
      }
      if (node == null) {
        result.add(e);
      } else {
        @SuppressWarnings("rawtypes")
        List childNodes = node.getChildNodes();
        //noinspection unchecked
        childNodes.add(e);
      }
    }
    return result;
  }

  /**
   * 返回当前节点的父节点id
   *
   * @return 当前对象的父id
   */
  @Nullable
  @Transient
  Object getParentNodeId();

  /**
   * 返回当前节点的唯一id
   *
   * @return 当前对象的唯一id
   */
  @Nonnull
  @Transient
  Object getNodeId();

  /**
   * 返回子节点列表, 必须提前初始化一个ArrayList
   *
   * @return 存储子节点的已实例化可变list容器
   */
  @Nonnull
  @Transient
  List<? extends TreeNode> getChildNodes();
}
