export type StarterFood = {
  nameTr: string;
  kcal: number;
  protein: number;
  carbs: number;
  fat: number;
  fiber: number;
};

// Nutrition values are approximate and represent 100g edible portion.
// Keep this list as the single source of truth for easy editing.
export const TURKISH_STARTER_FOODS: StarterFood[] = [
  { nameTr: "haşlanmış yumurta", kcal: 155, protein: 13, carbs: 1.1, fat: 11, fiber: 0 },
  { nameTr: "beyaz peynir", kcal: 265, protein: 14, carbs: 3.9, fat: 21, fiber: 0 },
  { nameTr: "lor peyniri", kcal: 98, protein: 17, carbs: 3.4, fat: 1.5, fiber: 0 },
  { nameTr: "kaşar peyniri", kcal: 404, protein: 25, carbs: 1.3, fat: 33, fiber: 0 },
  { nameTr: "yoğurt", kcal: 61, protein: 3.5, carbs: 4.7, fat: 3.3, fiber: 0 },
  { nameTr: "cacık", kcal: 45, protein: 2.1, carbs: 3.8, fat: 2.2, fiber: 0.3 },
  { nameTr: "tavuk göğsü", kcal: 165, protein: 31, carbs: 0, fat: 3.6, fiber: 0 },
  { nameTr: "tavuk but", kcal: 209, protein: 26, carbs: 0, fat: 10.9, fiber: 0 },
  { nameTr: "köfte", kcal: 252, protein: 16, carbs: 7.5, fat: 17, fiber: 0.8 },
  { nameTr: "pilav", kcal: 130, protein: 2.4, carbs: 28, fat: 0.3, fiber: 0.4 },
  { nameTr: "bulgur pilavı", kcal: 118, protein: 3.1, carbs: 24, fat: 1.1, fiber: 4.5 },
  { nameTr: "nohut", kcal: 164, protein: 8.9, carbs: 27.4, fat: 2.6, fiber: 7.6 },
  { nameTr: "kuru fasulye", kcal: 127, protein: 8.7, carbs: 22.8, fat: 0.5, fiber: 6.4 },
  { nameTr: "mercimek çorbası", kcal: 56, protein: 3.2, carbs: 8.3, fat: 1.3, fiber: 1.8 },
  { nameTr: "yayla çorbası", kcal: 63, protein: 2.4, carbs: 8.4, fat: 2.2, fiber: 0.4 },
  { nameTr: "sarma", kcal: 141, protein: 2.8, carbs: 17.5, fat: 6.8, fiber: 2.4 },
  { nameTr: "dolma", kcal: 119, protein: 3.1, carbs: 15.2, fat: 5.2, fiber: 2.6 },
  { nameTr: "poğaça", kcal: 320, protein: 8.4, carbs: 39, fat: 14.6, fiber: 1.7 },
  { nameTr: "simit", kcal: 289, protein: 9.2, carbs: 55.1, fat: 4.6, fiber: 3.2 },
  { nameTr: "ekmek", kcal: 265, protein: 8.8, carbs: 49.2, fat: 3.2, fiber: 2.7 },
  { nameTr: "elma", kcal: 52, protein: 0.3, carbs: 13.8, fat: 0.2, fiber: 2.4 },
  { nameTr: "muz", kcal: 89, protein: 1.1, carbs: 22.8, fat: 0.3, fiber: 2.6 },
  { nameTr: "portakal", kcal: 47, protein: 0.9, carbs: 11.8, fat: 0.1, fiber: 2.4 },
  { nameTr: "fındık", kcal: 628, protein: 15, carbs: 17, fat: 61, fiber: 10 },
  { nameTr: "badem", kcal: 579, protein: 21, carbs: 22, fat: 50, fiber: 12.5 },
  { nameTr: "ceviz", kcal: 654, protein: 15.2, carbs: 13.7, fat: 65.2, fiber: 6.7 },
  { nameTr: "ayran", kcal: 37, protein: 1.9, carbs: 2.9, fat: 1.8, fiber: 0 },
  { nameTr: "süt", kcal: 61, protein: 3.2, carbs: 4.8, fat: 3.3, fiber: 0 },
  { nameTr: "salata", kcal: 24, protein: 1.3, carbs: 4.1, fat: 0.3, fiber: 1.6 },
  { nameTr: "patates", kcal: 77, protein: 2, carbs: 17, fat: 0.1, fiber: 2.2 },
  { nameTr: "domates", kcal: 18, protein: 0.9, carbs: 3.9, fat: 0.2, fiber: 1.2 },
  { nameTr: "salatalık", kcal: 15, protein: 0.7, carbs: 3.6, fat: 0.1, fiber: 0.5 },
  { nameTr: "zeytin", kcal: 145, protein: 1, carbs: 3.8, fat: 15.3, fiber: 3.3 },
  { nameTr: "hummus", kcal: 166, protein: 7.9, carbs: 14.3, fat: 9.6, fiber: 6 },
  { nameTr: "menemen", kcal: 101, protein: 5.2, carbs: 3.8, fat: 7.1, fiber: 1.1 },
];
