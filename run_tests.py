#!/usr/bin/env python3
"""
Compile dan jalankan seluruh test case (TC-01..TC-04) untuk 5 studi kasus,
lalu bandingkan dengan expected output (cuplikan penting harus muncul berurutan).

Cara pakai (dari folder ini):
    python run_tests.py              # semua studi kasus
    python run_tests.py buku-tamu    # satu studi kasus saja

Hasil output asli tiap test case disimpan di <studi-kasus>/test-cases/TC-0X.output.txt
"""
import os
import subprocess
import sys

BASE = os.path.dirname(os.path.abspath(__file__))
CASES = ["catatan-keuangan", "buku-tamu", "inventaris-barang", "jadwal-kegiatan", "kontak-teman"]


def read_lines(path):
    with open(path, encoding="utf-8") as f:
        return [l.rstrip("\n") for l in f if l.strip()]


def matches_in_order(output, expected_lines):
    """Setiap baris expected harus muncul (berurutan) di dalam output."""
    pos = 0
    for line in expected_lines:
        found = output.find(line, pos)
        if found < 0:
            return False, line
        pos = found + len(line)
    return True, None


def run_case(case):
    case_dir = os.path.join(BASE, case)
    out_dir = os.path.join(case_dir, "output")
    comp = subprocess.run(
        ["javac", "-encoding", "UTF-8", "-d", out_dir, "-sourcepath", "src", os.path.join("src", "App.java")],
        cwd=case_dir, capture_output=True, text=True)
    if comp.returncode != 0:
        print(f"[{case}] GAGAL COMPILE\n{comp.stderr}")
        return 0, 4

    tc_dir = os.path.join(case_dir, "test-cases")
    passed = total = 0
    for tc in sorted(f for f in os.listdir(tc_dir) if f.endswith(".tc")):
        name = tc[:-3]
        total += 1
        with open(os.path.join(tc_dir, tc), encoding="utf-8") as stdin:
            run = subprocess.run(["java", "-Dfile.encoding=UTF-8", "-cp", "output", "App"], cwd=case_dir,
                                 stdin=stdin, capture_output=True, text=True, encoding="utf-8", timeout=30)
        with open(os.path.join(tc_dir, name + ".output.txt"), "w", encoding="utf-8") as f:
            f.write(run.stdout)
        ok, missing = matches_in_order(run.stdout, read_lines(os.path.join(tc_dir, name + ".expected.txt")))
        if ok and run.returncode == 0:
            passed += 1
            print(f"  PASS  {name}")
        else:
            print(f"  FAIL  {name}  -> baris tidak ditemukan: {missing!r}" if not ok else f"  FAIL  {name}  (exit code {run.returncode})")
    return passed, total


def main():
    selected = sys.argv[1:] or CASES
    all_pass = all_total = 0
    for case in selected:
        print(f"== {case}")
        p, t = run_case(case)
        all_pass += p
        all_total += t
    print(f"\nTotal: {all_pass}/{all_total} test case lulus")
    sys.exit(0 if all_pass == all_total else 1)


if __name__ == "__main__":
    main()
