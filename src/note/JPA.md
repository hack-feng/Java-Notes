### 动态构建查询SQL
~~~
Specification<Resources> specification = (root, criteriaQuery, criteriaBuilder) -> {
    List<Predicate> and = new ArrayList<>();
    // 查询指定channelId数据
    Path<Integer> delFlag = root.get("delFlag");
    and.add(criteriaBuilder.equal(delFlag, 0));

    return criteriaBuilder.and(and.toArray(new Predicate[0]));
};
~~~