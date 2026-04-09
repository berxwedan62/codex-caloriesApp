# Turkish Starter Food Seed (Lokma)

## What this provides

- A realistic Turkish starter dataset (`TURKISH_STARTER_FOODS`) with nutrition values per 100g.
- Turkish UI-friendly names via `nameTr`.
- First-launch seed flow via `seedTurkishFoodsOnFirstLaunch`.
- Duplicate-safe inserts with `UNIQUE(name_tr)` + `INSERT OR IGNORE`.

## Integration

Call once at app boot (example):

```ts
await seedTurkishFoodsOnFirstLaunch(db);
```

`db` should implement:

- `run(sql, params?) => Promise<void>`
- `getFirst(sql, params?) => Promise<Record<string, unknown> | null>`

## Editing nutrition later

Update only `src/nutrition/turkishStarterFoods.ts`.
The seeding function reads directly from this file.
