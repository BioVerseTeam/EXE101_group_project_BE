-- ============================================
-- V17: Admin-managed chemical reaction equations
-- (.chemx keyframe animations for Phản ứng lab)
-- ============================================

CREATE TABLE IF NOT EXISTS reaction_equations (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(100) NOT NULL,
    title           VARCHAR(255) NOT NULL,
    subtitle        VARCHAR(255),
    grade_label     VARCHAR(50),
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    chemx_json      JSONB NOT NULL,
    is_system       BOOLEAN NOT NULL DEFAULT FALSE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order      INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_reaction_equations_code UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS idx_reaction_equations_active
    ON reaction_equations (is_active, sort_order);

-- Seed built-in classroom samples (same as frontend REACTION_LESSONS)
INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'h2-cl2',
    'H₂ + Cl₂ → 2 HCl',
    'Hidro kết hợp clo tạo hidoclorua',
    'Lớp 8–9',
    'H2 + Cl2 → 2 HCl',
    'Hai phân tử hai nguyên tử tiến lại, đứt liên kết cũ và tạo hai phân tử HCl.',
    $chemx$
{
      "version": "1.0",
      "metadata": {
        "name": "H2 + Cl2 → 2 HCl",
        "description": "Hai phân tử hai nguyên tử tiến lại, đứt liên kết cũ và tạo hai phân tử HCl.",
        "created": 0
      },
      "duration": 4000,
      "keyframes": [
        {
          "timestamp": 0,
          "atoms": [
            {"id": "h1", "symbol": "H", "position": {"x": -3.2, "y": 0, "z": 0}, "charge": 0},
            {"id": "h2", "symbol": "H", "position": {"x": -2.4, "y": 0, "z": 0}, "charge": 0},
            {"id": "cl1", "symbol": "Cl", "position": {"x": 2.2, "y": 0, "z": 0}, "charge": 0},
            {"id": "cl2", "symbol": "Cl", "position": {"x": 4.0, "y": 0, "z": 0}, "charge": 0}
          ],
          "bonds": [
            {"id": "b_hh", "atomIds": ["h1", "h2"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_clcl", "atomIds": ["cl1", "cl2"], "order": 1, "strength": 1, "bondType": "covalent"}
          ]
        },
        {
          "timestamp": 2000,
          "atoms": [
            {"id": "h1", "symbol": "H", "position": {"x": -1.3, "y": 0, "z": 0.15}, "charge": 0},
            {"id": "h2", "symbol": "H", "position": {"x": -0.5, "y": 0, "z": -0.15}, "charge": 0},
            {"id": "cl1", "symbol": "Cl", "position": {"x": 0.4, "y": 0, "z": 0.2}, "charge": 0},
            {"id": "cl2", "symbol": "Cl", "position": {"x": 2.2, "y": 0, "z": -0.2}, "charge": 0}
          ],
          "bonds": [
            {"id": "b_hh", "atomIds": ["h1", "h2"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_clcl", "atomIds": ["cl1", "cl2"], "order": 1, "strength": 1, "bondType": "covalent"}
          ]
        },
        {
          "timestamp": 4000,
          "atoms": [
            {"id": "h1", "symbol": "H", "position": {"x": -0.7, "y": 0, "z": 0.7}, "charge": 0},
            {"id": "h2", "symbol": "H", "position": {"x": -0.7, "y": 0, "z": -0.7}, "charge": 0},
            {"id": "cl1", "symbol": "Cl", "position": {"x": 0.5, "y": 0, "z": 0.7}, "charge": 0},
            {"id": "cl2", "symbol": "Cl", "position": {"x": 0.5, "y": 0, "z": -0.7}, "charge": 0}
          ],
          "bonds": [
            {"id": "b_hcl1", "atomIds": ["h1", "cl1"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_hcl2", "atomIds": ["h2", "cl2"], "order": 1, "strength": 1, "bondType": "covalent"}
          ]
        }
      ]
    }$chemx$::jsonb,
    TRUE,
    1
)
ON CONFLICT (code) DO NOTHING;

INSERT INTO reaction_equations (code, title, subtitle, grade_label, name, description, chemx_json, is_system, sort_order)
VALUES (
    'h2-o2',
    '2 H₂ + O₂ → 2 H₂O',
    'Hidro cháy trong oxi tạo nước',
    'Lớp 8',
    '2 H2 + O2 → 2 H2O',
    'Hai phân tử hidro và một phân tử oxi tái sắp xếp thành hai phân tử nước.',
    $chemx$
{
      "version": "1.0",
      "metadata": {
        "name": "2 H2 + O2 → 2 H2O",
        "description": "Hai phân tử hidro và một phân tử oxi tái sắp xếp thành hai phân tử nước.",
        "created": 0
      },
      "duration": 4000,
      "keyframes": [
        {
          "timestamp": 0,
          "atoms": [
            {"id": "h1", "symbol": "H", "position": {"x": -3.4, "y": 0, "z": 0.8}, "charge": 0},
            {"id": "h2", "symbol": "H", "position": {"x": -2.6, "y": 0, "z": 0.8}, "charge": 0},
            {"id": "h3", "symbol": "H", "position": {"x": -3.4, "y": 0, "z": -0.8}, "charge": 0},
            {"id": "h4", "symbol": "H", "position": {"x": -2.6, "y": 0, "z": -0.8}, "charge": 0},
            {"id": "o1", "symbol": "O", "position": {"x": 2.2, "y": 0, "z": 0.3}, "charge": 0},
            {"id": "o2", "symbol": "O", "position": {"x": 3.6, "y": 0, "z": -0.3}, "charge": 0}
          ],
          "bonds": [
            {"id": "b_h2a", "atomIds": ["h1", "h2"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_h2b", "atomIds": ["h3", "h4"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_o2", "atomIds": ["o1", "o2"], "order": 1, "strength": 1, "bondType": "covalent"}
          ]
        },
        {
          "timestamp": 2000,
          "atoms": [
            {"id": "h1", "symbol": "H", "position": {"x": -1.2, "y": 0, "z": 0.7}, "charge": 0},
            {"id": "h2", "symbol": "H", "position": {"x": -0.5, "y": 0, "z": 0.7}, "charge": 0},
            {"id": "h3", "symbol": "H", "position": {"x": -1.2, "y": 0, "z": -0.7}, "charge": 0},
            {"id": "h4", "symbol": "H", "position": {"x": -0.5, "y": 0, "z": -0.7}, "charge": 0},
            {"id": "o1", "symbol": "O", "position": {"x": 0.5, "y": 0, "z": 0.4}, "charge": 0},
            {"id": "o2", "symbol": "O", "position": {"x": 1.9, "y": 0, "z": -0.4}, "charge": 0}
          ],
          "bonds": [
            {"id": "b_h2a", "atomIds": ["h1", "h2"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_h2b", "atomIds": ["h3", "h4"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_o2", "atomIds": ["o1", "o2"], "order": 1, "strength": 1, "bondType": "covalent"}
          ]
        },
        {
          "timestamp": 4000,
          "atoms": [
            {"id": "h1", "symbol": "H", "position": {"x": -0.7, "y": 0, "z": 1.0}, "charge": 0},
            {"id": "h2", "symbol": "H", "position": {"x": 0.5, "y": 0, "z": 1.0}, "charge": 0},
            {"id": "h3", "symbol": "H", "position": {"x": -0.7, "y": 0, "z": -1.0}, "charge": 0},
            {"id": "h4", "symbol": "H", "position": {"x": 0.5, "y": 0, "z": -1.0}, "charge": 0},
            {"id": "o1", "symbol": "O", "position": {"x": 0, "y": 0, "z": 0.7}, "charge": 0},
            {"id": "o2", "symbol": "O", "position": {"x": 0, "y": 0, "z": -0.7}, "charge": 0}
          ],
          "bonds": [
            {"id": "b_w1a", "atomIds": ["o1", "h1"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_w1b", "atomIds": ["o1", "h2"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_w2a", "atomIds": ["o2", "h3"], "order": 1, "strength": 1, "bondType": "covalent"},
            {"id": "b_w2b", "atomIds": ["o2", "h4"], "order": 1, "strength": 1, "bondType": "covalent"}
          ]
        }
      ]
    }$chemx$::jsonb,
    TRUE,
    2
)
ON CONFLICT (code) DO NOTHING;
