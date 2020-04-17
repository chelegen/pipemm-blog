package hello.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.graalvm.compiler.core.common.SuppressFBWarnings;

import java.time.Instant;

public class Blog {
    private Integer id;
    @JsonIgnore
    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "我希望把这个错误忽略掉")
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private boolean atIndex;
    private Instant createdAt;
    private Instant updatedAt;
    private User user;

    public boolean isAtIndex() {
        return atIndex;
    }

    public void setAtIndex(boolean atIndex) {
        this.atIndex = atIndex;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return user == null ? null : user.getId();
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
