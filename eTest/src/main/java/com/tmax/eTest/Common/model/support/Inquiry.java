package com.tmax.eTest.Common.model.support;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CS_INQUIRY")
@ToString(exclude = "inquiry_file")
public class Inquiry {

    @Id
    @SequenceGenerator(name = "INQUIRY_SEQ_GENERATOR", sequenceName = "INQUIRY_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INQUIRY_SEQ_GENERATOR")
    @Column(name = "INQUIRY_ID")
    private Long id;

    @JsonIgnoreProperties({ "inquiry" })
    @JoinColumn(name = "USER_UUID")
    @ManyToOne
    private UserMaster userMaster;

    @JsonIgnoreProperties({"inquiry"})
    @OneToMany(mappedBy = "inquiry", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Inquiry_file> inquiry_file;

    @Column(name = "INQUIRY_STATUS")
    private String status;

    @Column(name = "INQUIRY_TITLE")
    private String title;

    @Column(name = "INQUIRY_TYPE")
    private String type;

    @Column(name = "INQUIRY_CONTENT")
    private String content;

    @Column(name = "INQUIRY_ANSWER")
    private String answer;

    @Column(name = "INQUIRY_ANSWER_TIME")
    private LocalDateTime answer_time;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @PrePersist // 디비에 INSERT 되기 직전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
