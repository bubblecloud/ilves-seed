/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
package org.bubblecloud.ilves.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bubblecloud.ilves.model.Company;
import org.bubblecloud.ilves.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Feedback.
 *
 * @author Tommi S.E. Laukkanen
 */
@Entity
@Table(name = "comment")
public final class Comment implements Serializable {
    /** Java serialization version UID. */
    private static final long serialVersionUID = 1L;

    /** Unique UUID of the entity. */
    @Id
    @GeneratedValue(generator = "uuid")
    private String commentId;

    /** Owning company. */
    @JsonIgnore
    @JoinColumn(nullable = false)
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, optional = false)
    private Company owner;

    /** Comment author. */
    @JoinColumn(nullable = false)
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, optional = false)
    private User author;

    /** ID of the data item this comment is related to. */
    @Column(nullable = false)
    private String dataId;

    /** Message. */
    @Column(nullable = false)
    private String message;

    /** Created time of the task. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;

    /** Created time of the task. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date modified;

    /**
     * The default constructor for JPA.
     */
    public Comment() {
        super();
    }

    /**
     * Easy comment construction.
     *
     * @param owner the owning company (web site)
     * @param author the author
     * @param dataId the data ID
     * @param message the message
     */
    public Comment(final Company owner,final  User author,final String dataId, final  String message) {
        this.owner = owner;
        this.author = author;
        this.dataId = dataId;
        this.message = message;
        this.created = new Date();
        this.modified = this.created;
    }

    /**
     * @return the comment ID
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * @param commentId the comment ID to set
     */
    public void setCommentId(final String commentId) {
        this.commentId = commentId;
    }

    /**
     * @return the owning company
     */
    public Company getOwner() {
        return owner;
    }

    /**
     * @param owner the owning company to set
     */
    public void setOwner(final Company owner) {
        this.owner = owner;
    }

    /**
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * @param author the author
     */
    public void setAuthor(final User author) {
        this.author = author;
    }

    /**
     * @return the data ID
     */
    public String getDataId() {
        return dataId;
    }

    /**
     * @param dataId the data ID to set
     */
    public void setDataId(final String dataId) {
        this.dataId = dataId;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * @return the created time
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created time to set
     */
    public void setCreated(final Date created) {
        this.created = created;
    }

    /**
     * @return the modified time
     */
    public Date getModified() {
        return modified;
    }

    /**
     * @param modified the modified time to set
     */
    public void setModified(final Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return author.getEmailAddress() + " commented at " + created  + ": " + message;
    }

    @Override
    public int hashCode() {
        return commentId != null ? commentId.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj instanceof Comment && commentId.equals(((Comment) obj).commentId);
    }

}
