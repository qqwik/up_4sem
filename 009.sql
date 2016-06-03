select name, text, date from messages 
inner join users 
on messages.user_id = users.id and ( length(messages.text) >= 140);