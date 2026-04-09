import { TURKISH_STARTER_FOODS } from "./turkishStarterFoods";

const SEED_KEY = "starter_foods_seeded_v1";

type Row = Record<string, unknown>;

export type SeedDb = {
  run: (sql: string, params?: unknown[]) => Promise<void>;
  getFirst: (sql: string, params?: unknown[]) => Promise<Row | null>;
};

/**
 * Call this once during app initialization.
 * Seeds Turkish starter foods only on first launch.
 */
export async function seedTurkishFoodsOnFirstLaunch(db: SeedDb): Promise<void> {
  await db.run(`
    CREATE TABLE IF NOT EXISTS foods (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      name_tr TEXT NOT NULL UNIQUE,
      kcal REAL NOT NULL,
      protein REAL NOT NULL,
      carbs REAL NOT NULL,
      fat REAL NOT NULL,
      fiber REAL NOT NULL,
      created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
  `);

  await db.run(`
    CREATE TABLE IF NOT EXISTS app_meta (
      key TEXT PRIMARY KEY,
      value TEXT NOT NULL
    );
  `);

  const seeded = await db.getFirst(
    "SELECT value FROM app_meta WHERE key = ? LIMIT 1;",
    [SEED_KEY],
  );

  if (seeded?.value === "1") {
    return;
  }

  await db.run("BEGIN TRANSACTION;");
  try {
    for (const food of TURKISH_STARTER_FOODS) {
      await db.run(
        `
          INSERT OR IGNORE INTO foods (name_tr, kcal, protein, carbs, fat, fiber)
          VALUES (?, ?, ?, ?, ?, ?);
        `,
        [food.nameTr, food.kcal, food.protein, food.carbs, food.fat, food.fiber],
      );
    }

    await db.run(
      `
        INSERT INTO app_meta (key, value)
        VALUES (?, '1')
        ON CONFLICT(key) DO UPDATE SET value = excluded.value;
      `,
      [SEED_KEY],
    );

    await db.run("COMMIT;");
  } catch (error) {
    await db.run("ROLLBACK;");
    throw error;
  }
}
