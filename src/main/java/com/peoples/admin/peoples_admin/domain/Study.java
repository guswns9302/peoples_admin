package com.peoples.admin.peoples_admin.domain;

import com.peoples.admin.peoples_admin.domain.enumeration.Status;
import com.peoples.admin.peoples_admin.domain.enumeration.StudyCategory;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@AllArgsConstructor
@Table(name = "TB_STUDY")
@Builder
public class Study {
    @Id
    @Column(name = "STUDY_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long studyId;

    @Column(name = "STUDY_NAME")
    private String studyName;

    @Column(name = "STUDY_CATEGORY")
    @Enumerated(EnumType.STRING)
    private StudyCategory studyCategory;

    @Column(name = "STUDY_ON")
    private boolean studyOn;

    @Column(name = "STUDY_OFF")
    private boolean studyOff;

    @Column(name = "STUDY_INFO")
    private String studyInfo; // 한줄소개

    @Type(type = "json")
    @Column(name = "STUDY_RULE", columnDefinition = "json")
    private Map<String, Object> studyRule = new HashMap<>(); // 규칙

    @Column(name = "STUDY_FLOW")
    private String studyFlow; // 진행방식

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "FINISH_AT")
    private LocalDateTime finishAt;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "STUDY_BLOCK")
    private boolean studyBlock; // 영구 정지

    @Column(name = "STUDY_PAUSE")
    private boolean studyPause; // 일시 정지
}
