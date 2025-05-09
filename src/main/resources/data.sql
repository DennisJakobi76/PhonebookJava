INSERT INTO phonebook (id, first_name, last_name, phone_prefix, phone_number)
SELECT x.* FROM JSONB_ARRAY_ELEMENTS(
    (SELECT convert_from(pg_read_binary_file('classpath:data.json'), 'UTF8')::jsonb)
) WITH ORDINALITY AS arr(entry, id) 
CROSS JOIN LATERAL JSON_TO_RECORD(entry::json) AS x(
    id bigint, 
    first_name varchar, 
    last_name varchar, 
    phone_prefix varchar, 
    phone_number varchar
);