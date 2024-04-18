SET search_path TO artistdb;

-- Let's assume your answer requires two intermediate steps
-- (Note: Your solution does NOT have to use views, but it may
-- make your life easier to break down big problems this way)
DROP VIEW IF EXISTS YourViewNameHere1;
DROP VIEW IF EXISTS YourViewNameHere2;

-- e.g. you create the first view here to perform the first step
-- ..... --

-- e.g. you create the second view here to perform the first step
-- ..... --

-- etc. --

-- You SELECT the results here if it's a question that requires
-- you to report results, or INSERT/UPDATE/DELETE if it requires
-- you to update the database, etc.


-- Now drop the views you created earlier
DROP VIEW IF EXISTS YourViewNameHere2;
DROP VIEW IF EXISTS YourViewNameHere1;
