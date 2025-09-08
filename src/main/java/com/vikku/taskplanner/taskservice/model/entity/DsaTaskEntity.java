//package com.vikku.taskplanner.taskservice.model.entity;
//
//import com.vikku.taskplanner.common.model.entity.BaseEntity;
//import com.vikku.taskplanner.taskservice.model.enums.DsaTaskCategory;
//import com.vikku.taskplanner.taskservice.model.enums.TaskDifficulty;
//import com.vikku.taskplanner.taskservice.model.enums.TaskStaus;
//import jakarta.persistence.*;
//import lombok.*;
//
///**
// * DSA task entity class
// */
//@Entity
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "dsa_task")
//public class DsaTaskEntity extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long Id;
//
//    private String name;
//    private String problemId;
//    private String description;
//
//    @Enumerated(EnumType.STRING)
//    private DsaTaskCategory taskCategory;
//
//    @Enumerated(EnumType.STRING)
//    private TaskDifficulty difficultyCategory;
//
//    @Enumerated(EnumType.STRING)
//    private TaskStaus taskStaus;
//
//    private String url;
//}
