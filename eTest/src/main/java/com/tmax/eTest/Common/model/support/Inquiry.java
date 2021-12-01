package com.tmax.eTest.Common.model.support;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CS_INQUIRY")
public class Inquiry {

    @Id
    @SequenceGenerator(name = "INQUIRY_SEQ_GENERATOR", sequenceName = "INQUIRY_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INQUIRY_SEQ_GENERATOR")
    @Column(name = "INQUIRY_ID")
    private Long id;

    @JoinColumn(name = "USER_UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private UserMaster userMaster;

    @OneToMany(mappedBy = "inquiry", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonBackReference
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

    @Column(name = "ADMIN_UUID")
    private String adminUuid;

    @PrePersist // 디비에 INSERT 되기 직전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
