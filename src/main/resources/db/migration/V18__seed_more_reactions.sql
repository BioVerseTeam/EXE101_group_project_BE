-- ============================================
-- V18: Seed 8 additional classroom reaction equations
-- ============================================

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'n2-h2',
    'N₂ + 3 H₂ → 2 NH₃',
    'Tổng hợp amoniac (Haber)',
    'Lớp 9',
    'N2 + 3 H2 → 2 NH3',
    'Phân tử nitơ và hidro tiến lại, đứt liên kết cũ và tạo hai phân tử amoniac.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "N2 + 3 H2 → 2 NH3",
    "description": "Phân tử nitơ và hidro tiến lại, đứt liên kết cũ và tạo hai phân tử amoniac.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "n1",
          "symbol": "N",
          "position": {
            "x": -2.8,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "n2",
          "symbol": "N",
          "position": {
            "x": -1.4,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        },
        {
          "id": "h1",
          "symbol": "H",
          "position": {
            "x": 1.6,
            "y": 0,
            "z": 1.2
          },
          "charge": 0
        },
        {
          "id": "h2",
          "symbol": "H",
          "position": {
            "x": 2.4,
            "y": 0,
            "z": 1.2
          },
          "charge": 0
        },
        {
          "id": "h3",
          "symbol": "H",
          "position": {
            "x": 1.6,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "h4",
          "symbol": "H",
          "position": {
            "x": 2.4,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "h5",
          "symbol": "H",
          "position": {
            "x": 1.6,
            "y": 0,
            "z": -1.2
          },
          "charge": 0
        },
        {
          "id": "h6",
          "symbol": "H",
          "position": {
            "x": 2.4,
            "y": 0,
            "z": -1.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_n2",
          "atomIds": [
            "n1",
            "n2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_h1",
          "atomIds": [
            "h1",
            "h2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_h2",
          "atomIds": [
            "h3",
            "h4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_h3",
          "atomIds": [
            "h5",
            "h6"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "n1",
          "symbol": "N",
          "position": {
            "x": -1,
            "y": 0,
            "z": 0.3
          },
          "charge": 0
        },
        {
          "id": "n2",
          "symbol": "N",
          "position": {
            "x": 0.2,
            "y": 0,
            "z": -0.3
          },
          "charge": 0
        },
        {
          "id": "h1",
          "symbol": "H",
          "position": {
            "x": 0.8,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "h2",
          "symbol": "H",
          "position": {
            "x": 1.5,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "h3",
          "symbol": "H",
          "position": {
            "x": 0.8,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "h4",
          "symbol": "H",
          "position": {
            "x": 1.5,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "h5",
          "symbol": "H",
          "position": {
            "x": 0.8,
            "y": 0,
            "z": -1
          },
          "charge": 0
        },
        {
          "id": "h6",
          "symbol": "H",
          "position": {
            "x": 1.5,
            "y": 0,
            "z": -1
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_n2",
          "atomIds": [
            "n1",
            "n2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_h1",
          "atomIds": [
            "h1",
            "h2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_h2",
          "atomIds": [
            "h3",
            "h4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_h3",
          "atomIds": [
            "h5",
            "h6"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "n1",
          "symbol": "N",
          "position": {
            "x": -1.2,
            "y": 0,
            "z": 0.8
          },
          "charge": 0
        },
        {
          "id": "n2",
          "symbol": "N",
          "position": {
            "x": 1.2,
            "y": 0,
            "z": -0.8
          },
          "charge": 0
        },
        {
          "id": "h1",
          "symbol": "H",
          "position": {
            "x": -2,
            "y": 0,
            "z": 1.4
          },
          "charge": 0
        },
        {
          "id": "h2",
          "symbol": "H",
          "position": {
            "x": -0.4,
            "y": 0,
            "z": 1.4
          },
          "charge": 0
        },
        {
          "id": "h3",
          "symbol": "H",
          "position": {
            "x": -1.2,
            "y": 0,
            "z": 0.1
          },
          "charge": 0
        },
        {
          "id": "h4",
          "symbol": "H",
          "position": {
            "x": 2,
            "y": 0,
            "z": -1.4
          },
          "charge": 0
        },
        {
          "id": "h5",
          "symbol": "H",
          "position": {
            "x": 0.4,
            "y": 0,
            "z": -1.4
          },
          "charge": 0
        },
        {
          "id": "h6",
          "symbol": "H",
          "position": {
            "x": 1.2,
            "y": 0,
            "z": -0.1
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_nh1",
          "atomIds": [
            "n1",
            "h1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_nh2",
          "atomIds": [
            "n1",
            "h2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_nh3",
          "atomIds": [
            "n1",
            "h3"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_nh4",
          "atomIds": [
            "n2",
            "h4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_nh5",
          "atomIds": [
            "n2",
            "h5"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_nh6",
          "atomIds": [
            "n2",
            "h6"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    3
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'h2-f2',
    'H₂ + F₂ → 2 HF',
    'Hidro kết hợp flo tạo hidroflo',
    'Lớp 8–9',
    'H2 + F2 → 2 HF',
    'Hai phân tử hai nguyên tử tiến lại và tạo hai phân tử HF.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "H2 + F2 → 2 HF",
    "description": "Hai phân tử hai nguyên tử tiến lại và tạo hai phân tử HF.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "h1",
          "symbol": "H",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "h2",
          "symbol": "H",
          "position": {
            "x": -2.4,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "f1",
          "symbol": "F",
          "position": {
            "x": 2.2,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "f2",
          "symbol": "F",
          "position": {
            "x": 3.5,
            "y": 0,
            "z": 0
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_hh",
          "atomIds": [
            "h1",
            "h2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_ff",
          "atomIds": [
            "f1",
            "f2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "h1",
          "symbol": "H",
          "position": {
            "x": -1.2,
            "y": 0,
            "z": 0.15
          },
          "charge": 0
        },
        {
          "id": "h2",
          "symbol": "H",
          "position": {
            "x": -0.4,
            "y": 0,
            "z": -0.15
          },
          "charge": 0
        },
        {
          "id": "f1",
          "symbol": "F",
          "position": {
            "x": 0.5,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "f2",
          "symbol": "F",
          "position": {
            "x": 1.8,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_hh",
          "atomIds": [
            "h1",
            "h2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_ff",
          "atomIds": [
            "f1",
            "f2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "h1",
          "symbol": "H",
          "position": {
            "x": -0.7,
            "y": 0,
            "z": 0.7
          },
          "charge": 0
        },
        {
          "id": "h2",
          "symbol": "H",
          "position": {
            "x": -0.7,
            "y": 0,
            "z": -0.7
          },
          "charge": 0
        },
        {
          "id": "f1",
          "symbol": "F",
          "position": {
            "x": 0.5,
            "y": 0,
            "z": 0.7
          },
          "charge": 0
        },
        {
          "id": "f2",
          "symbol": "F",
          "position": {
            "x": 0.5,
            "y": 0,
            "z": -0.7
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_hf1",
          "atomIds": [
            "h1",
            "f1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_hf2",
          "atomIds": [
            "h2",
            "f2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    4
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'c-o2',
    'C + O₂ → CO₂',
    'Cacbon cháy trong oxi tạo khí cácbonic',
    'Lớp 8',
    'C + O2 → CO2',
    'Nguyên tử cacbon kết hợp phân tử oxi thành phân tử CO₂ thẳng.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "C + O2 → CO2",
    "description": "Nguyên tử cacbon kết hợp phân tử oxi thành phân tử CO₂ thẳng.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "c1",
          "symbol": "C",
          "position": {
            "x": -2.8,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 1.8,
            "y": 0,
            "z": 0.25
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 3.1,
            "y": 0,
            "z": -0.25
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_o2",
          "atomIds": [
            "o1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "c1",
          "symbol": "C",
          "position": {
            "x": -0.8,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 0.6,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 1.9,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_o2",
          "atomIds": [
            "o1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "c1",
          "symbol": "C",
          "position": {
            "x": 0,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": -1.4,
            "y": 0,
            "z": 0
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 1.4,
            "y": 0,
            "z": 0
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_co1",
          "atomIds": [
            "c1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_co2",
          "atomIds": [
            "c1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    5
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'n2-o2',
    'N₂ + O₂ → 2 NO',
    'Nitơ oxi hóa tạo nitơ monoxit',
    'Lớp 9',
    'N2 + O2 → 2 NO',
    'Hai phân tử hai nguyên tử tái sắp xếp thành hai phân tử NO.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "N2 + O2 → 2 NO",
    "description": "Hai phân tử hai nguyên tử tái sắp xếp thành hai phân tử NO.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "n1",
          "symbol": "N",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "n2",
          "symbol": "N",
          "position": {
            "x": -1.9,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 1.8,
            "y": 0,
            "z": 0.25
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 3.1,
            "y": 0,
            "z": -0.25
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_n2",
          "atomIds": [
            "n1",
            "n2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_o2",
          "atomIds": [
            "o1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "n1",
          "symbol": "N",
          "position": {
            "x": -1.3,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "n2",
          "symbol": "N",
          "position": {
            "x": -0.2,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 0.5,
            "y": 0,
            "z": 0.25
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 1.8,
            "y": 0,
            "z": -0.25
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_n2",
          "atomIds": [
            "n1",
            "n2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_o2",
          "atomIds": [
            "o1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "n1",
          "symbol": "N",
          "position": {
            "x": -0.9,
            "y": 0,
            "z": 0.7
          },
          "charge": 0
        },
        {
          "id": "n2",
          "symbol": "N",
          "position": {
            "x": -0.9,
            "y": 0,
            "z": -0.7
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 0.5,
            "y": 0,
            "z": 0.7
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 0.5,
            "y": 0,
            "z": -0.7
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_no1",
          "atomIds": [
            "n1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_no2",
          "atomIds": [
            "n2",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    6
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'na-cl2',
    '2 Na + Cl₂ → 2 NaCl',
    'Natri kết hợp clo tạo muối ăn',
    'Lớp 8–9',
    '2 Na + Cl2 → 2 NaCl',
    'Hai nguyên tử natri và một phân tử clo tạo hai đơn vị NaCl.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "2 Na + Cl2 → 2 NaCl",
    "description": "Hai nguyên tử natri và một phân tử clo tạo hai đơn vị NaCl.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "na1",
          "symbol": "Na",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": 0.8
          },
          "charge": 0
        },
        {
          "id": "na2",
          "symbol": "Na",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": -0.8
          },
          "charge": 0
        },
        {
          "id": "cl1",
          "symbol": "Cl",
          "position": {
            "x": 2,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "cl2",
          "symbol": "Cl",
          "position": {
            "x": 3.7,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_clcl",
          "atomIds": [
            "cl1",
            "cl2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "na1",
          "symbol": "Na",
          "position": {
            "x": -1.2,
            "y": 0,
            "z": 0.7
          },
          "charge": 0
        },
        {
          "id": "na2",
          "symbol": "Na",
          "position": {
            "x": -1.2,
            "y": 0,
            "z": -0.7
          },
          "charge": 0
        },
        {
          "id": "cl1",
          "symbol": "Cl",
          "position": {
            "x": 0.6,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "cl2",
          "symbol": "Cl",
          "position": {
            "x": 2.2,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_clcl",
          "atomIds": [
            "cl1",
            "cl2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "na1",
          "symbol": "Na",
          "position": {
            "x": -1,
            "y": 0,
            "z": 0.9
          },
          "charge": 0
        },
        {
          "id": "na2",
          "symbol": "Na",
          "position": {
            "x": -1,
            "y": 0,
            "z": -0.9
          },
          "charge": 0
        },
        {
          "id": "cl1",
          "symbol": "Cl",
          "position": {
            "x": 0.6,
            "y": 0,
            "z": 0.9
          },
          "charge": 0
        },
        {
          "id": "cl2",
          "symbol": "Cl",
          "position": {
            "x": 0.6,
            "y": 0,
            "z": -0.9
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_nacl1",
          "atomIds": [
            "na1",
            "cl1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "ionic"
        },
        {
          "id": "b_nacl2",
          "atomIds": [
            "na2",
            "cl2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "ionic"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    7
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'mg-o2',
    '2 Mg + O₂ → 2 MgO',
    'Magie cháy trong oxi tạo oxit magie',
    'Lớp 8',
    '2 Mg + O2 → 2 MgO',
    'Hai nguyên tử magie và một phân tử oxi tạo hai đơn vị MgO.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "2 Mg + O2 → 2 MgO",
    "description": "Hai nguyên tử magie và một phân tử oxi tạo hai đơn vị MgO.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "mg1",
          "symbol": "Mg",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": 0.8
          },
          "charge": 0
        },
        {
          "id": "mg2",
          "symbol": "Mg",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": -0.8
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 1.8,
            "y": 0,
            "z": 0.25
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 3.1,
            "y": 0,
            "z": -0.25
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_o2",
          "atomIds": [
            "o1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "mg1",
          "symbol": "Mg",
          "position": {
            "x": -1.2,
            "y": 0,
            "z": 0.7
          },
          "charge": 0
        },
        {
          "id": "mg2",
          "symbol": "Mg",
          "position": {
            "x": -1.2,
            "y": 0,
            "z": -0.7
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 0.5,
            "y": 0,
            "z": 0.25
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 1.8,
            "y": 0,
            "z": -0.25
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_o2",
          "atomIds": [
            "o1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "mg1",
          "symbol": "Mg",
          "position": {
            "x": -0.9,
            "y": 0,
            "z": 0.85
          },
          "charge": 0
        },
        {
          "id": "mg2",
          "symbol": "Mg",
          "position": {
            "x": -0.9,
            "y": 0,
            "z": -0.85
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": 0.6,
            "y": 0,
            "z": 0.85
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 0.6,
            "y": 0,
            "z": -0.85
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_mgo1",
          "atomIds": [
            "mg1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "ionic"
        },
        {
          "id": "b_mgo2",
          "atomIds": [
            "mg2",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "ionic"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    8
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'co-o2',
    '2 CO + O₂ → 2 CO₂',
    'Cacbon monoxit cháy thành cácbonic',
    'Lớp 9',
    '2 CO + O2 → 2 CO2',
    'Hai phân tử CO và một phân tử O₂ tạo hai phân tử CO₂.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "2 CO + O2 → 2 CO2",
    "description": "Hai phân tử CO và một phân tử O₂ tạo hai phân tử CO₂.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "c1",
          "symbol": "C",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": 0.9
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": -1.9,
            "y": 0,
            "z": 0.9
          },
          "charge": 0
        },
        {
          "id": "c2",
          "symbol": "C",
          "position": {
            "x": -3.2,
            "y": 0,
            "z": -0.9
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": -1.9,
            "y": 0,
            "z": -0.9
          },
          "charge": 0
        },
        {
          "id": "o3",
          "symbol": "O",
          "position": {
            "x": 2,
            "y": 0,
            "z": 0.25
          },
          "charge": 0
        },
        {
          "id": "o4",
          "symbol": "O",
          "position": {
            "x": 3.3,
            "y": 0,
            "z": -0.25
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_co1",
          "atomIds": [
            "c1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_co2",
          "atomIds": [
            "c2",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_o2",
          "atomIds": [
            "o3",
            "o4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "c1",
          "symbol": "C",
          "position": {
            "x": -1.4,
            "y": 0,
            "z": 0.8
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": -0.2,
            "y": 0,
            "z": 0.8
          },
          "charge": 0
        },
        {
          "id": "c2",
          "symbol": "C",
          "position": {
            "x": -1.4,
            "y": 0,
            "z": -0.8
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": -0.2,
            "y": 0,
            "z": -0.8
          },
          "charge": 0
        },
        {
          "id": "o3",
          "symbol": "O",
          "position": {
            "x": 0.8,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "o4",
          "symbol": "O",
          "position": {
            "x": 2,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_co1",
          "atomIds": [
            "c1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_co2",
          "atomIds": [
            "c2",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_o2",
          "atomIds": [
            "o3",
            "o4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "c1",
          "symbol": "C",
          "position": {
            "x": -0.2,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": -1.5,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "o3",
          "symbol": "O",
          "position": {
            "x": 1.1,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "c2",
          "symbol": "C",
          "position": {
            "x": -0.2,
            "y": 0,
            "z": -1
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": -1.5,
            "y": 0,
            "z": -1
          },
          "charge": 0
        },
        {
          "id": "o4",
          "symbol": "O",
          "position": {
            "x": 1.1,
            "y": 0,
            "z": -1
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_a1",
          "atomIds": [
            "c1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_a2",
          "atomIds": [
            "c1",
            "o3"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_b1",
          "atomIds": [
            "c2",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_b2",
          "atomIds": [
            "c2",
            "o4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    9
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'so2-o2',
    '2 SO₂ + O₂ → 2 SO₃',
    'Lưu huỳnh dioxit oxi hóa thành SO₃',
    'Lớp 9',
    '2 SO2 + O2 → 2 SO3',
    'Hai phân tử SO₂ và một phân tử O₂ tạo hai phân tử SO₃.',
    $chemx$
{
  "version": "1.0",
  "metadata": {
    "name": "2 SO2 + O2 → 2 SO3",
    "description": "Hai phân tử SO₂ và một phân tử O₂ tạo hai phân tử SO₃.",
    "created": 0
  },
  "duration": 4000,
  "keyframes": [
    {
      "timestamp": 0,
      "atoms": [
        {
          "id": "s1",
          "symbol": "S",
          "position": {
            "x": -3,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": -4.2,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": -1.8,
            "y": 0,
            "z": 1
          },
          "charge": 0
        },
        {
          "id": "s2",
          "symbol": "S",
          "position": {
            "x": -3,
            "y": 0,
            "z": -1
          },
          "charge": 0
        },
        {
          "id": "o3",
          "symbol": "O",
          "position": {
            "x": -4.2,
            "y": 0,
            "z": -1
          },
          "charge": 0
        },
        {
          "id": "o4",
          "symbol": "O",
          "position": {
            "x": -1.8,
            "y": 0,
            "z": -1
          },
          "charge": 0
        },
        {
          "id": "o5",
          "symbol": "O",
          "position": {
            "x": 2,
            "y": 0,
            "z": 0.25
          },
          "charge": 0
        },
        {
          "id": "o6",
          "symbol": "O",
          "position": {
            "x": 3.3,
            "y": 0,
            "z": -0.25
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_s1a",
          "atomIds": [
            "s1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_s1b",
          "atomIds": [
            "s1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_s2a",
          "atomIds": [
            "s2",
            "o3"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_s2b",
          "atomIds": [
            "s2",
            "o4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_o2",
          "atomIds": [
            "o5",
            "o6"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 2000,
      "atoms": [
        {
          "id": "s1",
          "symbol": "S",
          "position": {
            "x": -1.4,
            "y": 0,
            "z": 0.9
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": -2.5,
            "y": 0,
            "z": 0.9
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": -0.3,
            "y": 0,
            "z": 0.9
          },
          "charge": 0
        },
        {
          "id": "s2",
          "symbol": "S",
          "position": {
            "x": -1.4,
            "y": 0,
            "z": -0.9
          },
          "charge": 0
        },
        {
          "id": "o3",
          "symbol": "O",
          "position": {
            "x": -2.5,
            "y": 0,
            "z": -0.9
          },
          "charge": 0
        },
        {
          "id": "o4",
          "symbol": "O",
          "position": {
            "x": -0.3,
            "y": 0,
            "z": -0.9
          },
          "charge": 0
        },
        {
          "id": "o5",
          "symbol": "O",
          "position": {
            "x": 0.8,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "o6",
          "symbol": "O",
          "position": {
            "x": 2,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_s1a",
          "atomIds": [
            "s1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_s1b",
          "atomIds": [
            "s1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_s2a",
          "atomIds": [
            "s2",
            "o3"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_s2b",
          "atomIds": [
            "s2",
            "o4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_o2",
          "atomIds": [
            "o5",
            "o6"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    },
    {
      "timestamp": 4000,
      "atoms": [
        {
          "id": "s1",
          "symbol": "S",
          "position": {
            "x": -0.4,
            "y": 0,
            "z": 1.1
          },
          "charge": 0
        },
        {
          "id": "o1",
          "symbol": "O",
          "position": {
            "x": -1.6,
            "y": 0,
            "z": 1.5
          },
          "charge": 0
        },
        {
          "id": "o2",
          "symbol": "O",
          "position": {
            "x": 0.8,
            "y": 0,
            "z": 1.5
          },
          "charge": 0
        },
        {
          "id": "o5",
          "symbol": "O",
          "position": {
            "x": -0.4,
            "y": 0,
            "z": 0.2
          },
          "charge": 0
        },
        {
          "id": "s2",
          "symbol": "S",
          "position": {
            "x": -0.4,
            "y": 0,
            "z": -1.1
          },
          "charge": 0
        },
        {
          "id": "o3",
          "symbol": "O",
          "position": {
            "x": -1.6,
            "y": 0,
            "z": -1.5
          },
          "charge": 0
        },
        {
          "id": "o4",
          "symbol": "O",
          "position": {
            "x": 0.8,
            "y": 0,
            "z": -1.5
          },
          "charge": 0
        },
        {
          "id": "o6",
          "symbol": "O",
          "position": {
            "x": -0.4,
            "y": 0,
            "z": -0.2
          },
          "charge": 0
        }
      ],
      "bonds": [
        {
          "id": "b_a1",
          "atomIds": [
            "s1",
            "o1"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_a2",
          "atomIds": [
            "s1",
            "o2"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_a3",
          "atomIds": [
            "s1",
            "o5"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_b1",
          "atomIds": [
            "s2",
            "o3"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_b2",
          "atomIds": [
            "s2",
            "o4"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        },
        {
          "id": "b_b3",
          "atomIds": [
            "s2",
            "o6"
          ],
          "order": 1,
          "strength": 1,
          "bondType": "covalent"
        }
      ]
    }
  ]
}
$chemx$::jsonb,
    TRUE,
    10
)
ON CONFLICT (code) DO NOTHING;

