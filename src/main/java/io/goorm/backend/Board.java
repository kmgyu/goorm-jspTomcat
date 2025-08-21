package io.goorm.backend;

import java.sql.Timestamp;
import java.util.List;

/**
 * Board Value Object
 * 2000년대 초반 J2EE 패턴 스타일
 */
public class Board {
  private Long id;
  private String title;
  private String content;
  private String author;
  private Timestamp createdAt;
  private int writerId;
  private List<FileUpload> attachments;

  // 기본 생성자
  public Board() {
  }

  // 매개변수 생성자
  public Board(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }

  // Getter 메서드들
  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getAuthor() {
    return author;
  }

  public Timestamp getCreatedAt() { return createdAt; }


  public int getWriterId() {
    return writerId;
  }

  // Setter 메서드들
  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public void setWriterId(int writerId) { this.writerId = writerId; }


  public List<FileUpload> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<FileUpload> attachments) {
    this.attachments = attachments;
  }

  public void addAttachment(FileUpload fileUpload) {
    this.attachments.add(fileUpload);
  }

  public void removeAttachment(FileUpload fileUpload) {
    this.attachments.remove(fileUpload);
  }

  // toString 메서드
  @Override
  public String toString() {
    return "Board{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", author='" + author + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
