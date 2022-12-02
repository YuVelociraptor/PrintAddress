# PrintAddress

- 環境変数
```
export ADDRESS_SQLITE=SQLITEファイルへのパス
```

- テーブルDDL
```
create table main.to_info
(
    id       INTEGER
        constraint to_info_pk
            primary key,
    zip_code TEXT,
    address1 TEXT,
    address2 TEXT,
    name     TEXT
);
```