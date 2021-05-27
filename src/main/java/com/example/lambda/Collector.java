package com.example.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Collector {

    @Data
    @AllArgsConstructor
    public static class User {

        public enum UserType {
            STUDENT, TEACHER
        }

        //unique id
        private Integer id;
        private UserType type;
        private String name;
        private List<String> skills;

    }

    private static final List<User> USERS = new ArrayList<>();

    static {
        USERS.add(new User(1, User.UserType.STUDENT, "Jack", List.of("singing")));
        USERS.add(new User(2, User.UserType.TEACHER, "Rose", List.of("swimming", "dancing")));
        USERS.add(new User(3, User.UserType.STUDENT, "Henry", List.of("drawing")));
    }

    public static Map<Integer, User> collectMapById() {
        return USERS.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }

    public static Map<User.UserType, List<User>> collectMapByType() {
        return USERS.stream().collect(Collectors.toMap(User::getType, List::of, (l, r) -> {
            List<User> list = new ArrayList<>();
            list.addAll(l);
            list.addAll(r);
            return list;
        }));
    }

    public static Map<User.UserType, List<User>> groupByType() {
        return USERS.stream().collect(Collectors.groupingBy(User::getType, Collectors.toList()));
    }

    public static Map<User.UserType, List<Integer>> groupByTypeSelectId() {
        return USERS.stream().collect(Collectors.groupingBy(User::getType, Collectors.mapping(User::getId, Collectors.toList())));
    }

    public static Map<Boolean, List<User>> partitionByType() {
        return USERS.stream().collect(Collectors.partitioningBy(u -> u.getType() == User.UserType.STUDENT));
    }

    public static Map<Boolean, Map<Integer, User>> partitionByTypeToMapById() {
        return USERS.stream().collect(Collectors.partitioningBy(u -> u.getType() == User.UserType.STUDENT,
                Collectors.toMap(User::getId, Function.identity())));
    }

    public static IntSummaryStatistics summaryNameLen() {
        return USERS.stream().collect(Collectors.summarizingInt(u -> u.getName().length()));
    }

    public static List<String> getAllSkills() {
        //flatMap会聚集多个stream成为1个stream
        return USERS.stream().flatMap(u -> u.getSkills().stream()).collect(Collectors.toList());
    }

    public static String joiningAllName() {
        return USERS.stream().map(User::getName).collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        //stream api使得可以很方便地从一种集合转为另外一种集合
        System.out.println(collectMapById());
        System.out.println(collectMapByType());
        System.out.println(groupByType());
        System.out.println(groupByTypeSelectId());
        System.out.println(partitionByType());
        System.out.println(partitionByTypeToMapById());
        System.out.println(summaryNameLen());
        System.out.println(getAllSkills());
        System.out.println(joiningAllName());
    }

}
