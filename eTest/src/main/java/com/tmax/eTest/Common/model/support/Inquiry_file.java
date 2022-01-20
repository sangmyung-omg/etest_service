package com.tmax.eTest.Common.model.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "CS_INQUIRY_FILE")
public class Inquiry_file {

    @Id
    @SequenceGenerator(name = "INQUIRY_FILE_SEQ_GENERATOR", sequenceName = "INQUIRY_FILE_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INQUIRY_FILE_SEQ_GENERATOR")
    @Column(name = "INQUIRY_FILE_ID")
    private Long id;

    @Column(name = "INQUIRY_FILE_ENCODING")
    private String imageEncoding;

    @Column(name = "INQUIRY_FILE_TYPE")
    private String type;

    @Column(name = "INQUIRY_FILE_NAME")
    private String name;

    @Column(name = "INQUIRY_FILE_SIZE")
    private Long size;

    @ManyToOne
    @JoinColumn(name = "INQUIRY_ID")
    @JsonIgnore
    private Inquiry inquiry;

}