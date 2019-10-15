package id.ac.unja.klikunja.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notices
{
    @SerializedName("home_page_url")
    @Expose
    private String home_page_url;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("feed_url")
    @Expose
    private String feed_url;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("items")
    @Expose
    private List<Items> items = null;

    @SerializedName("user_comment")
    @Expose
    private String user_comment;

    public String getHome_page_url ()
    {
        return home_page_url;
    }

    public void setHome_page_url (String home_page_url)
    {
        this.home_page_url = home_page_url;
    }

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getFeed_url ()
    {
        return feed_url;
    }

    public void setFeed_url (String feed_url)
    {
        this.feed_url = feed_url;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    public List<Items> getItems ()
    {
        return items;
    }

    public void setItems (List<Items> items)
    {
        this.items = items;
    }

    public String getUser_comment ()
    {
        return user_comment;
    }

    public void setUser_comment (String user_comment)
    {
        this.user_comment = user_comment;
    }


    public class Items
    {
        @SerializedName("date_modified")
        @Expose
        private String date_modified;

        @SerializedName("date_published")
        @Expose
        private String date_published;

        @SerializedName("author")
        @Expose
        private Author author;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("url")
        @Expose
        private String url;

        public String getDate_modified ()
        {
            return date_modified;
        }

        public void setDate_modified (String date_modified)
        {
            this.date_modified = date_modified;
        }

        public String getDate_published ()
        {
            return date_published;
        }

        public void setDate_published (String date_published)
        {
            this.date_published = date_published;
        }

        public Author getAuthor ()
        {
            return author;
        }

        public void setAuthor (Author author)
        {
            this.author = author;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getUrl ()
        {
            return url;
        }

        public void setUrl (String url)
        {
            this.url = url;
        }

    }


    public class Author
    {
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("avatar")
        @Expose
        private String avatar;

        @SerializedName("url")
        @Expose
        private String url;

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getAvatar ()
        {
            return avatar;
        }

        public void setAvatar (String avatar)
        {
            this.avatar = avatar;
        }

        public String getUrl ()
        {
            return url;
        }

        public void setUrl (String url)
        {
            this.url = url;
        }

    }
}