CREATE TABLE IF NOT EXISTS item
(
    id      SERIAL PRIMARY KEY,
    title   TEXT      NOT NULL UNIQUE,
    body    TEXT      NOT NULL,
    author  TEXT      NOT NULL,
    created TIMESTAMP NOT NULL

);

CREATE TABLE IF NOT EXISTS comment
(
    id          SERIAL PRIMARY KEY,
    commentator TEXT NOT NULL,
    body        TEXT NOT NULL,
    item_id     INT REFERENCES item (id)

);

comment on table comment is 'Комментарий к новости';
comment on column comment.id is 'Идентификатор комментария';
comment on column comment.commentator is 'Имя комментатора';
comment on column comment.body is 'Текст комментария';
comment on column comment.item_id is 'Id новости, к которой относится комментарий';


comment on table item is 'Создаваемая новость';
comment on column item.id is 'Идентификатор новости';
comment on column item.title is 'Заголовк новости';
comment on column item.body is 'Текст новости';
comment on column item.author is 'Автор новости';
comment on column item.created is 'Дата создания новости (по UTC+0)';
