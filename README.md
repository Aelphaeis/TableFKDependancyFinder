# TableFKDependancyFinder

This is a utility current used to generate a script to delete an entry from a table given the primary key, table name and schema. Unfortunately for this utility to be used the following criteria must be met:

1. All tables in the database should have one primary key
2. There should be no circular dependencies within the database.
3. The database driver must be MySql
