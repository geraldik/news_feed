CREATE TABLE IF NOT EXISTS item
(
    id         SERIAL PRIMARY KEY,
    title      TEXT      NOT NULL UNIQUE,
    body       TEXT      NOT NULL,
    author     TEXT      NOT NULL,
    created    TIMESTAMP NOT NULL,
    disable BOOLEAN default FALSE

);

CREATE TABLE IF NOT EXISTS comment
(
    id          SERIAL PRIMARY KEY,
    commentator TEXT NOT NULL,
    body        TEXT NOT NULL,
    item_id     INT REFERENCES item (id)

);

comment on table comment is 'Comment on the news';
comment on column comment.id is 'Comment id';
comment on column comment.commentator is 'Commentator name';
comment on column comment.body is 'Comment text';
comment on column comment.item_id is 'Id of the news to which the comment belongs';


comment on table item is 'Created news';
comment on column item.id is 'News id';
comment on column item.title is 'Title of the news ';
comment on column item.body is 'News text';
comment on column item.author is 'News author';
comment on column item.created is 'News creation date (UTC+0)';
comment on column item.disable is 'Mark news for disabling';
