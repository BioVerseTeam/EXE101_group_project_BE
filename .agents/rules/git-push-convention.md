# Git Push Convention — BioVerse Team

> **BẮT BUỘC**: Mỗi khi user yêu cầu commit / push / PR / merge, agent phải **đọc hết file này trước**, rồi mới chạy git. Cursor không được bỏ qua bước này.

---

## 0. Việc đầu tiên (trước mọi `git commit` / `git push`)

Chạy các lệnh sau và đọc output. Không commit khi author/committer sai.

```bash
git rev-parse --show-toplevel
git status -sb
git branch -vv
git config user.name
git config user.email
```

Identity bắt buộc của repo này:

| Field | Value |
|-------|--------|
| `user.name` | `huynh van hao` |
| `user.email` | `magicmath2k5@gmail.com` |

Email Cursor (`hvycommon@gmail.com`, `cursoragent@cursor.com`, `noreply`) **không** được xuất hiện ở Author hoặc Committer.

---

## 1. Cursor / AI KHÔNG được tính contribution trên GitHub

GitHub tính contributor theo **Author email**, **Committer email**, và trailer `Co-authored-by` / `Made-with`.

Agent **cấm**:

- Thêm `Co-authored-by: Cursor`
- Thêm `Made-with: Cursor` / `Made with Cursor`
- Dùng `git commit --trailer`
- Dùng `--author` với tên/email của Cursor, Copilot, agent
- Commit bằng email Cursor account
- Sửa `git config` (user.name / user.email) — dùng env cho **một** lệnh commit

Cách commit đúng (identity developer, không trailer):

```bash
GIT_AUTHOR_NAME="huynh van hao" \
GIT_AUTHOR_EMAIL="magicmath2k5@gmail.com" \
GIT_COMMITTER_NAME="huynh van hao" \
GIT_COMMITTER_EMAIL="magicmath2k5@gmail.com" \
git commit -m "$(cat <<'EOF'
feat(scope): short description

Optional body.
EOF
)"
```

Ngay sau commit, **kiểm tra**:

```bash
git log -1 --format='%an <%ae>%n%cn <%ce>%n%B'
```

Nếu thấy `Cursor`, `cursoragent`, `Co-authored-by`, `Made-with`, `Made with Cursor`:

1. **Chưa push** → `git commit --amend` để xóa trailer, giữ nguyên author/committer developer (chỉ amend commit vừa tạo trong session này).
2. **Đã push** → không amend; báo user, tạo commit mới nếu cần.

Cursor IDE/CLI **vẫn có thể tự inject** trailer dù agent không ghi. Bắt buộc:

1. Cài hook (một lần mỗi clone):

```bash
cp scripts/githooks/commit-msg .git/hooks/commit-msg
chmod +x .git/hooks/commit-msg
```

Hook này xóa `Co-authored-by: Cursor` và `Made-with: Cursor` trước khi commit hoàn tất.

2. User tắt Attribution (một lần):
- IDE: **Cursor Settings → Git & PRs → Attribution** → tắt Commit Attribution và PR Attribution
- CLI: `~/.cursor/cli-config.json` → `"attributeCommitsToAgent": false`, `"attributePRsToAgent": false`

---

## 2. GitFlow

```
main          production — KHÔNG push trực tiếp
 └── dev      integration — nhận PR từ feature
      ├── feat/<tên-feature>
      ├── fix/<tên-bug>
      ├── hotfix/<tên-hotfix>   ← branch từ main, merge về main và dev
      ├── chore/<tên-task>
      └── docs/<tên-docs>
```

### Quy tắc branch

- Feature/fix/chore/docs: **tạo từ `origin/dev`**, tên kebab-case (`feat/database-schema`)
- Hotfix: tạo từ `origin/main`
- Push **nhánh hiện tại** lên `origin`. Không push `main`. Không push `dev` trừ khi user yêu cầu rõ release.
- Không force push (`-f` / `--force`) trừ khi user yêu cầu rõ.
- Merge vào `dev` / `main` qua **Pull Request**, không merge local rồi push thẳng.

### Flow khi user nói "push code"

```bash
# 1. Đúng nhánh feature (không phải main)
git checkout feat/<tên-feature>

# 2. Lấy latest của nhánh đích (thường là dev), ưu tiên rebase
git fetch origin
git pull origin dev --rebase

# 3. Commit (identity ở mục 1) rồi push nhánh feature
git push -u origin HEAD

# 4. Mở PR: feat/* → dev (không → main)
gh pr create --base dev --head feat/<tên-feature> --title "<type>(<scope>): <description>"
```

Nếu nhánh local **lệch history** so với `origin/<cùng-tên>`: không force-push. Báo user. Ưu tiên commit phần việc mới **trên** `origin/<branch>` hiện có.

---

## 3. Conventional Commits

```
<type>(<scope>): <short description>

[optional body]
```

| Type | Khi nào |
|------|---------|
| `feat` | Chức năng mới |
| `fix` | Sửa bug |
| `docs` | Tài liệu |
| `style` | Format, không đổi logic |
| `refactor` | Đổi code, không feat/fix |
| `test` | Test |
| `chore` | Config, deps, việc vặt |
| `perf` | Hiệu năng |
| `ci` | CI/CD |
| `build` | Build system |
| `revert` | Revert commit |

Scope thường dùng: `auth`, `user`, `payment`, `subscription`, `exam`, `experiment`, `chemical`, `bio-model`, `3d`, `ai`, `config`, `db`, `api`, `deps`, `git`

Message:

- Tiếng Anh, imperative: `add`, `fix`, `update` — không `added` / `fixed`
- Không viết hoa chữ đầu description, không dấu chấm cuối
- Subject ≤ 72 ký tự
- Body cách subject 1 dòng trống

Ví dụ:

```
feat(db): add user auth and subscription schema migrations V2-V10
```

---

## 4. Checklist trước khi push

- [ ] Đã đọc file này
- [ ] Author + Committer = `huynh van hao <magicmath2k5@gmail.com>`
- [ ] Log commit không có Cursor / Co-authored-by / Made-with
- [ ] Đang ở nhánh feature/fix, **không** phải `main`
- [ ] Message đúng Conventional Commits
- [ ] Không stage `.env`, `serviceAccountKey.json`, credentials, `node_modules/`, `target/`
- [ ] Đã `git fetch` và rebase/merge với nhánh đích khi cần
- [ ] Push `origin <feature-branch>`, PR vào `dev`

---

## 5. Cấm với AI agent (Cursor)

1. Không bao giờ để Cursor thành author, committer, hoặc co-author
2. Không merge thẳng vào `main`
3. Không force push trừ khi user yêu cầu
4. Không commit secrets
5. Không đổi `git config`
6. Feature branch mới luôn từ `origin/dev` (hotfix từ `main`)
7. Khi user chỉ nói "push": commit nếu có thay đổi hợp lệ, push nhánh feature, tạo PR → `dev` nếu chưa có
