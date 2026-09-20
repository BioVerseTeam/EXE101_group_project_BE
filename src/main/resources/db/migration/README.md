# Flyway migrations — immutable

**Never edit a migration that has already been applied** on any shared database (Supabase prod/staging, teammate DBs).

Flyway stores a checksum of each file in `flyway_schema_history`. Changing an old `V*.sql` causes:

```text
Migration checksum mismatch for migration version N
```

and the app will not start.

## Rules

1. Need schema/data changes → add a **new** file: `V19__short_description.sql`, `V20__...`, etc.
2. Do not rename, reorder, or rewrite `V1`…`VN` that already shipped.
3. Do not add `IF NOT EXISTS` / cosmetic edits to old files to “fix” local runs.
4. Local reset: drop schema / use a fresh DB, or `flyway clean` only on disposable local DBs — never on prod.

## If checksum mismatch already happened

Prefer restoring the SQL file to the exact content that was applied (git history).

Only if that is impossible, update history once on that database (Supabase SQL):

```sql
-- Replace checksum with the "Resolved locally" value from the app log
UPDATE flyway_schema_history
SET checksum = <resolved_locally>
WHERE version = '<N>';
```

Do not enable `spring.flyway.repair-on-migrate=true` permanently in prod.
