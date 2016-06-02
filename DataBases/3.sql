
SET @id = (SELECT id  FROM chat.users  WHERE name = 'user1');

select text from chat.Messages where date = '2016-05-09' and user_id = @id ;