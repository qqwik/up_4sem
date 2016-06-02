SET @id = (SELECT id  FROM chat.users  WHERE name = 'user1');

select text from chat.Messages as m where m.user_id = @id and m.text LIKE '%hello%';