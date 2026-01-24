# DEV-19: GitHub Free Account Rules & PR / Branch Restrictions

## Overview
This document investigates the rules and restrictions available in GitHub Free accounts,
focusing on pull requests, branch protection rules, and CI/CD enforcement.

---

## Branch Protection Rules Available on GitHub Free

GitHub Free accounts support essential branch protection rules that help maintain code quality
and prevent direct changes to critical branches.

The following rules are available:

- Require pull request before merging
- Require at least two approval
- Dismiss stale approvals when new commits are pushed
- Require status checks to pass before merging
- Require branches to be up to date before merging
- Restrict force pushes
- Require linear history
- Include administrators

---

## Enforcement on Main Branch

All the above branch protection rules can be applied to the `main` branch.
Once enabled, the `main` branch cannot be directly pushed to and requires
review and passing CI checks before any merge.

---

## Limitations Compared to Paid GitHub Plans

GitHub Free has some limitations compared to paid plans:

- No merge queue support
- No advanced branch rulesets
- Cannot require reviews from specific teams or users
- No deployment protection rules
- Limited audit and compliance features

These advanced controls are available only on paid GitHub plans.

---

## How to Configure Branch Protection (GitHub UI)

1. Open the repository on GitHub
2. Navigate to **Settings**
3. Select **Branches** from the sidebar
4. Click **Add branch protection rule**
5. Enter `main` as the branch name pattern
6. Enable the desired protection options
7. Save the rule

---

## CI/CD Enforcement Using GitHub Actions (Optional)

GitHub Actions can be used alongside branch protection rules to enforce automated checks.

A CI workflow can be configured to run on pull requests and block merging
if tests or builds fail. Branch protection rules can then require this
workflow to pass before allowing a merge.

---

## Conclusion

GitHub Free provides sufficient branch protection and pull request enforcement
features for small teams and individual projects. While advanced governance
features are limited to paid plans, combining branch protection rules with
GitHub Actions allows effective quality control even on the free tier.
