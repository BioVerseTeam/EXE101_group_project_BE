# Agent instructions

Khi user yêu cầu **commit, push, PR, merge** (đặc biệt Cursor): đọc và tuân thủ **trước tiên**:

**`.agents/rules/git-push-convention.md`**

Tóm tắt:

- GitFlow: feature branch từ `dev`, push nhánh feature, PR vào `dev`, không push `main`.
- Conventional Commits: `type(scope): message`.
- Cursor không được là Author / Committer / Co-author trên GitHub.
